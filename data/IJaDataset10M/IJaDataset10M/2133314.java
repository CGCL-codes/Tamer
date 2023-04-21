package com.exult.android;

import java.util.Vector;
import android.graphics.Point;
import java.io.OutputStream;
import java.io.IOException;

public class BargeObject extends ContainerGameObject implements TimeSensitive {

    private int timeQueueCount;

    private Vector<GameObject> objects;

    private int permCount;

    private int xtiles, ytiles;

    private int dir;

    private boolean gathered;

    private boolean iceRaft;

    private boolean firstStep;

    private boolean taking2ndStep;

    private int boat;

    private int frameTime;

    private PathFinder path;

    private Tile center;

    private Tile pos, eventPos;

    private Point loc;

    private Rectangle footprint, newfoot, dirty;

    GameObject getObject(int i) {
        return objects.elementAt(i);
    }

    private static void Rotate90r(Tile result, Tile t, Tile c) {
        int rx = t.tx - c.tx, ry = c.ty - t.ty;
        result.set(c.tx + ry, c.ty + rx, t.tz);
    }

    private static void Rotate90l(Tile result, Tile t, Tile c) {
        int rx = t.tx - c.tx, ry = c.ty - t.ty;
        result.set(c.tx - ry, c.ty - rx, t.tz);
    }

    private static void Rotate180(Tile result, Tile t, Tile c) {
        int rx = t.tx - c.tx, ry = c.ty - t.ty;
        result.set(c.tx - rx, c.ty + ry, t.tz);
    }

    private Tile Rotate90r(Tile result, int xtiles, int ytiles, Tile c) {
        getTile(result);
        Rotate90r(result, result, c);
        result.tx = (short) ((result.tx + ytiles + EConst.c_num_tiles) % EConst.c_num_tiles);
        result.ty = (short) ((result.ty + EConst.c_num_tiles) % EConst.c_num_tiles);
        return result;
    }

    private Tile Rotate90l(Tile result, int xtiles, int ytiles, Tile c) {
        getTile(result);
        Rotate90l(result, result, c);
        result.ty = (short) ((result.ty + xtiles + EConst.c_num_tiles) % EConst.c_num_tiles);
        result.tx = (short) ((result.tx + EConst.c_num_tiles) % EConst.c_num_tiles);
        return result;
    }

    private Tile Rotate180(Tile result, int xtiles, int ytiles, Tile c) {
        getTile(result);
        Rotate180(result, result, c);
        result.tx = (short) ((result.tx + xtiles + EConst.c_num_tiles) % EConst.c_num_tiles);
        result.ty = (short) ((result.ty + ytiles + EConst.c_num_tiles) % EConst.c_num_tiles);
        return result;
    }

    private void swapDims() {
        int tmp = xtiles;
        xtiles = ytiles;
        ytiles = tmp;
    }

    private void setCenter() {
        getTile(center);
        center.tx = (short) ((center.tx - xtiles / 2 + EConst.c_num_tiles) % EConst.c_num_tiles);
        center.ty = (short) ((center.ty - ytiles / 2 + EConst.c_num_tiles) % EConst.c_num_tiles);
    }

    private boolean okayToRotate(Tile pos) {
        int lift = getLift();
        int move_type = (lift > 0) ? (EConst.MOVE_LEVITATE) : EConst.MOVE_ALL_TERRAIN;
        Rectangle foot = getTileFootprint();
        int xts = xtiles, yts = ytiles;
        newfoot.set(pos.tx - yts + 1, pos.ty - xts + 1, yts, xts);
        if (newfoot.y < foot.y) {
            pos.set(newfoot.x, newfoot.y, lift);
            if (!MapChunk.areaAvailable(newfoot.w, foot.y - newfoot.y, 4, pos, move_type, 0) || pos.tz != lift) return false;
        }
        if (foot.y + foot.h < newfoot.y + newfoot.h) {
            pos.set(newfoot.x, foot.y + foot.h, lift);
            if (!MapChunk.areaAvailable(newfoot.w, newfoot.y + newfoot.h - (foot.y + foot.h), 4, pos, move_type, 0) || pos.tz != lift) return false;
        }
        if (newfoot.x < foot.x) {
            pos.set(newfoot.x, newfoot.y, lift);
            if (!MapChunk.areaAvailable(foot.x - newfoot.x, newfoot.h, 4, pos, move_type, 0) || pos.tz != lift) return false;
        }
        if (foot.x + foot.w < newfoot.x + newfoot.w) pos.set(foot.x + foot.w, newfoot.y, lift);
        if (!MapChunk.areaAvailable(newfoot.x + newfoot.w - (foot.x + foot.w), newfoot.h, 4, pos, move_type, 0) || pos.tz != lift) return false;
        return true;
    }

    private void addDirty() {
        gwin.getShapeLocation(loc, this);
        int w = xtiles * EConst.c_tilesize, h = ytiles * EConst.c_tilesize;
        dirty.set(loc.x - w, loc.y - h, w, h);
        final int barge_enlarge = (EConst.c_tilesize + EConst.c_tilesize / 4);
        final int barge_stretch = (4 * EConst.c_tilesize + EConst.c_tilesize / 2);
        dirty.enlarge(barge_enlarge);
        if (dir % 2 != 0) {
            dirty.x -= barge_enlarge / 2;
            dirty.w += barge_stretch;
        } else {
            dirty.y -= barge_enlarge / 2;
            dirty.h += barge_stretch;
        }
        gwin.clipToWin(dirty);
        gwin.addDirty(dirty);
    }

    private void finishMove(Tile positions[], int newmap) {
        setCenter();
        int cnt = objects.size();
        for (int i = 0; i < cnt; i++) {
            GameObject obj = getObject(i);
            if (i < permCount) obj.setOwner(this);
            obj.move(positions[i].tx, positions[i].ty, positions[i].tz, newmap);
        }
        gwin.scrollIfNeeded(center);
    }

    public BargeObject(int shapenum, int framenum, int shapex, int shapey, int lft, int xt, int yt, int d) {
        super(shapenum, framenum, shapex, shapey, lft, 0);
        xtiles = xt;
        ytiles = yt;
        dir = d;
        firstStep = true;
        boat = -1;
        center = new Tile();
        pos = new Tile();
        eventPos = new Tile();
        footprint = new Rectangle();
        newfoot = new Rectangle();
        dirty = new Rectangle();
        loc = new Point();
        objects = new Vector<GameObject>();
    }

    public Rectangle getTileFootprint() {
        int tx = getTileX(), ty = getTileY();
        int xts = xtiles, yts = ytiles;
        footprint.set((tx - xts + 1 + EConst.c_num_tiles) % EConst.c_num_tiles, (ty - yts + 1 + EConst.c_num_tiles) % EConst.c_num_tiles, xts, yts);
        return footprint;
    }

    public boolean isMoving() {
        return frameTime > 0;
    }

    public int getXtiles() {
        return xtiles;
    }

    public int getYtiles() {
        return ytiles;
    }

    public Tile getCenter() {
        return center;
    }

    public void setToGather() {
        gathered = false;
    }

    public void gather() {
        if (gmap.getChunk(getCx(), getCy()) == null) return;
        iceRaft = false;
        objects.setSize(permCount);
        Rectangle foot = getTileFootprint();
        int lift = getLift();
        MapChunk.ChunkIntersectIterator iter = new MapChunk.ChunkIntersectIterator(foot);
        Rectangle tiles = new Rectangle();
        MapChunk chunk;
        while ((chunk = iter.getNext(tiles)) != null) {
            tiles.x += chunk.getCx() * EConst.c_tiles_per_chunk;
            tiles.y += chunk.getCy() * EConst.c_tiles_per_chunk;
            GameObject obj;
            ObjectList.ObjectIterator next = new ObjectList.ObjectIterator(chunk.getObjects());
            while ((obj = next.next()) != null) {
                if (obj == this) continue;
                if (obj.isEgg()) continue;
                Tile t = pos;
                obj.getTile(t);
                if (!tiles.hasPoint(t.tx, t.ty) || obj.getOwner() == this) continue;
                ShapeInfo info = obj.getInfo();
                boolean isbarge = info.isBargePart();
                if (t.tz + info.get3dHeight() > lift && ((isbarge && t.tz >= lift - 1) || (t.tz < lift + 5 && t.tz >= lift))) {
                    objects.add(obj);
                    int btype = obj.getInfo().getBargeType();
                    if (btype == ShapeInfo.barge_raft) iceRaft = true; else if (btype == ShapeInfo.barge_turtle) xtiles = 20;
                }
            }
        }
        setCenter();
        chunk = gmap.getChunk(center.tx / EConst.c_tiles_per_chunk, center.ty / EConst.c_tiles_per_chunk);
        if (boat == -1 && chunk != null) {
            ChunkTerrain ter = chunk.getTerrain();
            int flatShape = ter.getShapeNum(center.tx % EConst.c_tiles_per_chunk, center.ty % EConst.c_tiles_per_chunk);
            ShapeInfo info = ShapeID.getInfo(flatShape);
            boat = info.isWater() ? 1 : 0;
        }
        gathered = true;
    }

    public void faceDirection(int ndir) {
        ndir /= 2;
        switch((4 + ndir - dir) % 4) {
            case 1:
                turnRight();
                break;
            case 2:
                turnAround();
                break;
            case 3:
                turnLeft();
                break;
            default:
                break;
        }
    }

    public void travelToTile(Tile dest, int speed) {
        if (path == null) path = new ZombiePathFinder();
        Tile t = new Tile();
        getTile(t);
        if (path.NewPath(t, dest)) {
            frameTime = speed;
            int curtx = getTileX(), curty = getTileY();
            int dy = Tile.delta(curty, dest.ty), dx = Tile.delta(curtx, dest.tx);
            int ndir = EUtil.getDirection4(-dy, dx);
            if (!iceRaft) faceDirection(ndir);
            if (!inQueue()) tqueue.add(TimeQueue.ticks, this, null);
        } else frameTime = 0;
    }

    public void turnRight() {
        addDirty();
        Rotate90r(pos, xtiles, ytiles, center);
        if (!okayToRotate(pos)) return;
        super.move(pos.tx, pos.ty, pos.tz);
        swapDims();
        dir = (dir + 1) % 4;
        int cnt = objects.size();
        Tile positions[] = new Tile[cnt];
        for (int i = 0; i < cnt; i++) {
            GameObject obj = getObject(i);
            int frame = obj.getFrameNum();
            ShapeInfo info = obj.getInfo();
            positions[i] = Rotate90r(new Tile(), info.get3dXtiles(frame), info.get3dYtiles(frame), center);
            obj.removeThis();
            obj.setFrame(obj.getRotatedFrame(1));
            obj.setInvalid();
        }
        finishMove(positions, -1);
    }

    public void turnLeft() {
        addDirty();
        Rotate90l(pos, xtiles, ytiles, center);
        if (!okayToRotate(pos)) return;
        super.move(pos.tx, pos.ty, pos.tz);
        swapDims();
        dir = (dir + 3) % 4;
        int cnt = objects.size();
        Tile positions[] = new Tile[cnt];
        for (int i = 0; i < cnt; i++) {
            GameObject obj = getObject(i);
            int frame = obj.getFrameNum();
            ShapeInfo info = obj.getInfo();
            positions[i] = Rotate90l(new Tile(), info.get3dXtiles(frame), info.get3dYtiles(frame), center);
            obj.removeThis();
            obj.setFrame(obj.getRotatedFrame(3));
            obj.setInvalid();
        }
        finishMove(positions, -1);
    }

    public void turnAround() {
        addDirty();
        Rotate180(pos, xtiles, ytiles, center);
        super.move(pos.tx, pos.ty, pos.tz);
        dir = (dir + 2) % 4;
        int cnt = objects.size();
        Tile positions[] = new Tile[cnt];
        for (int i = 0; i < cnt; i++) {
            GameObject obj = getObject(i);
            int frame = obj.getFrameNum();
            ShapeInfo info = obj.getInfo();
            positions[i] = Rotate180(new Tile(), info.get3dXtiles(frame), info.get3dYtiles(frame), center);
            obj.removeThis();
            obj.setFrame(obj.getRotatedFrame(2));
            obj.setInvalid();
        }
        finishMove(positions, -1);
    }

    public void stop() {
        frameTime = 0;
        firstStep = true;
    }

    private static int norecurse = 0;

    public void done() {
        gathered = false;
        if (norecurse > 0) return;
        norecurse++;
        if (boat == 1) {
            int cnt = objects.size();
            for (int i = 0; i < cnt; i++) {
                GameObject obj = objects.elementAt(i);
                if (obj.getInfo().getBargeType() == ShapeInfo.barge_sails && (obj.getFrameNum() & 7) < 4) {
                    obj.activate();
                    break;
                }
            }
        }
        norecurse--;
    }

    public boolean okayToLand() {
        Rectangle foot = getTileFootprint();
        int lift = getLift();
        MapChunk.ChunkIntersectIterator iter = new MapChunk.ChunkIntersectIterator(foot);
        Rectangle tiles = newfoot;
        MapChunk chunk;
        while ((chunk = iter.getNext(tiles)) != null) {
            for (int ty = tiles.y; ty < tiles.y + tiles.h; ty++) for (int tx = tiles.x; tx < tiles.x + tiles.w; tx++) if (chunk.getHighestBlocked(lift, tx, ty) != -1) return false;
        }
        return true;
    }

    @Override
    public BargeObject asBarge() {
        return this;
    }

    @Override
    public void move(int newtx, int newty, int newlift, int newmap) {
        if (chunk == null) {
            super.move(newtx, newty, newlift, newmap);
            return;
        }
        if (!gathered) gather();
        addDirty();
        Tile old = pos;
        if (newmap == -1) newmap = getMapNum();
        super.move(newtx, newty, newlift, newmap);
        int dx = newtx - old.tx, dy = newty - old.ty, dz = newlift - old.tz;
        int cnt = objects.size();
        Tile positions[] = new Tile[cnt];
        int i;
        for (i = 0; i < cnt; i++) {
            GameObject obj = getObject(i);
            Tile ot = new Tile();
            obj.getTile(ot);
            positions[i] = ot;
            ot.set((ot.tx + dx + EConst.c_num_tiles) % EConst.c_num_tiles, (ot.ty + dy + EConst.c_num_tiles) % EConst.c_num_tiles, ot.tz + dz);
            obj.removeThis();
            obj.setInvalid();
            if (!taking2ndStep) {
                int frame = obj.getFrameNum();
                switch(obj.getInfo().getBargeType()) {
                    case ShapeInfo.barge_wheel:
                        obj.setFrame(((frame + 1) & 3) | (frame & 32));
                        break;
                    case ShapeInfo.barge_draftanimal:
                        obj.setFrame(((frame + 4) & 15) | (frame & 32));
                        break;
                }
            }
        }
        finishMove(positions, newmap);
    }

    @Override
    public void remove(GameObject obj) {
        obj.setOwner(null);
        obj.removeThis();
    }

    @Override
    public boolean add(GameObject obj, boolean dont_check, boolean combine, boolean noset) {
        objects.add(obj);
        return (false);
    }

    public final boolean contains(GameObject obj) {
        return objects.contains(obj);
    }

    @Override
    public boolean drop(GameObject obj) {
        return false;
    }

    @Override
    public void paint() {
        if (gwin.paintEggs) {
            super.paint();
            byte pix = ShapeID.getSpecialPixel(ShapeID.CURSED_PIXEL);
            int lx, ty;
            gwin.getShapeLocation(loc, this);
            lx = loc.x - xtiles * EConst.c_tilesize + 1;
            ty = loc.y - ytiles * EConst.c_tilesize + 1;
            gwin.getWin().fill8(pix, 4, 4, loc.x - 2, loc.y - 2);
            gwin.getWin().fill8(pix, 4, 4, lx - 1, ty - 1);
            gwin.getWin().fill8(pix, xtiles * EConst.c_tilesize, 1, lx, ty);
            gwin.getWin().fill8(pix, xtiles * EConst.c_tilesize, 1, lx, loc.y);
            gwin.getWin().fill8(pix, 1, ytiles * EConst.c_tilesize, lx, ty);
            gwin.getWin().fill8(pix, 1, ytiles * EConst.c_tilesize, loc.x, ty);
        }
    }

    @Override
    public void activate(int event) {
    }

    @Override
    public boolean step(Tile t, int frame, boolean force) {
        if (!gathered) gather();
        getTile(pos);
        int move_type;
        if (pos.tz > 0) move_type = EConst.MOVE_LEVITATE; else if (force) move_type = EConst.MOVE_ALL; else if (boat == 1) move_type = EConst.MOVE_SWIM; else move_type = EConst.MOVE_WALK;
        if (!MapChunk.areaAvailable(getXtiles(), getYtiles(), 4, pos, t, move_type, 0, 0)) return false;
        move(t.tx, t.ty, t.tz);
        MapChunk nlist = gmap.getChunk(getCx(), getCy());
        nlist.activateEggs(gwin.getMainActor(), t.tx, t.ty, t.tz, pos.tx, pos.ty, false);
        return true;
    }

    @Override
    public void writeIreg(OutputStream out) throws IOException {
        byte buf[] = new byte[20];
        int ind = writeCommonIreg(12, buf);
        buf[ind++] = (byte) xtiles;
        buf[ind++] = (byte) ytiles;
        buf[ind++] = 0;
        buf[ind++] = (byte) ((dir << 1) | (((gwin.getMovingBarge() == this) ? 1 : 0) << 3));
        buf[ind++] = 0;
        buf[ind++] = (byte) (((int) getLift() & 15) << 4);
        buf[ind++] = 0;
        buf[ind++] = 0;
        out.write(buf, 0, ind);
        for (int i = 0; i < permCount; i++) {
            GameObject obj = getObject(i);
            obj.writeIreg(out);
        }
        out.write(0x01);
        GameMap.writeScheduled(out, this, false);
    }

    @Override
    public int getIregSize() {
        if (gwin.getMovingBarge() == this || UsecodeScript.find(this) != null) return -1;
        int total_size = 8 + getCommonIregSize();
        for (int i = 0; i < permCount; i++) {
            GameObject obj = getObject(i);
            int size = obj.getIregSize();
            if (size < 0) return -1;
            total_size += size;
        }
        total_size += 1;
        return total_size;
    }

    @Override
    public void elementsRead() {
        permCount = 0;
    }

    @Override
    public void addedToQueue() {
        ++timeQueueCount;
    }

    @Override
    public boolean alwaysHandle() {
        return false;
    }

    @Override
    public void handleEvent(int ctime, Object udata) {
        if (path == null || frameTime == 0 || gwin.getMovingBarge() != this) return;
        if (!path.getNextStep(eventPos) || !step(eventPos, -1, false)) frameTime = 0; else if (!firstStep) {
            taking2ndStep = true;
            if (!path.getNextStep(eventPos) || !step(eventPos, -1, false)) frameTime = 0;
            taking2ndStep = false;
        }
        if (frameTime > 0) tqueue.add(ctime + frameTime, this, udata);
        firstStep = false;
    }

    @Override
    public void removedFromQueue() {
        --timeQueueCount;
    }

    public boolean inQueue() {
        return timeQueueCount > 0;
    }
}
