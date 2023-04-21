package emap;

import java.util.ArrayList;

class ObjectDrawn {

    private int myImagePointer;

    private int myX;

    private int myY;

    private int myLevel;

    public ObjectDrawn() {
        myImagePointer = 0;
        myX = 0;
        myY = 0;
        myLevel = 0;
    }

    public ObjectDrawn(int whatimage, int wherex, int wherey, int wlevel) {
        myImagePointer = whatimage;
        myX = wherex;
        myY = wherey;
        myLevel = wlevel;
    }

    public ObjectDrawn(int wherex, int wherey, int wlevel, int whichgraphic, int whichdir, int whichframe) {
        myImagePointer = whichgraphic * 16 + whichdir * 4 + whichframe;
        myX = wherex;
        myY = wherey;
        myLevel = wlevel;
    }

    public int getImgPtr() {
        return myImagePointer;
    }

    public void setImgPtr(int a) {
        myImagePointer = a;
    }

    public int getX() {
        return myX;
    }

    public void setX(int a) {
        myX = a;
    }

    public int getY() {
        return myY;
    }

    public void setY(int a) {
        myY = a;
    }

    public int getLevel() {
        return myLevel;
    }

    public void setLevel(int a) {
        myLevel = a;
    }

    public void animate() {
        int whichimg = myImagePointer;
        int whichgraphic = whichimg / 16;
        whichimg = whichimg - (whichgraphic * 16);
        int whichdir = whichimg / 4;
        whichimg = whichimg - (whichdir * 4);
        int whichframe = whichimg;
        whichframe++;
        if (whichframe > 3) whichframe = 0;
        if (whichframe < 0) whichframe = 3;
        myImagePointer = whichgraphic * 16 + whichdir * 4 + whichframe;
    }

    public void reface(int newdir) {
        int whichimg = myImagePointer;
        int whichgraphic = whichimg / 16;
        whichimg = whichimg - (whichgraphic * 16);
        int whichdir = whichimg / 4;
        whichimg = whichimg - (whichdir * 4);
        myImagePointer = whichgraphic * 16 + newdir * 4 + whichimg;
    }

    public void moveBy(int xs, int ys, Coord mapDim) {
        myX += xs;
        myY += ys;
        if (myX < 0) myX = 0;
        if (myY < 0) myY = 0;
        if (myX + 32 > mapDim.X) myX = mapDim.X - 32;
        if (myY + 48 > mapDim.Y) myY = mapDim.Y - 48;
    }
}

class ObjectDrawnList {

    private ArrayList<ArrayList<ObjectDrawn>> objectlist;

    private int numlevels;

    public ObjectDrawnList() {
        numlevels = 1;
        resetobjectlist();
    }

    public ObjectDrawnList(int newheight) {
        numlevels = newheight;
        resetobjectlist();
    }

    public void resetobjectlist() {
        objectlist = new ArrayList<ArrayList<ObjectDrawn>>();
        for (int a = 0; a < numlevels; a++) objectlist.add(new ArrayList<ObjectDrawn>());
    }

    public void Resize(int newheight) {
        numlevels = newheight;
        resetobjectlist();
    }

    public void AddObj(ObjectDrawn toadd) {
        if (toadd == null) return;
        ObjectDrawn newod = new ObjectDrawn(toadd.getImgPtr(), toadd.getX(), toadd.getY(), toadd.getLevel());
        int heightof = newod.getLevel();
        if (heightof < 0) return;
        if (heightof >= numlevels) return;
        objectlist.get(heightof).add(newod);
    }

    public void AddObjAdv(int wherex, int wherey, int wlevel, int whichgraphic, int whichdir, int whichframe) {
        ObjectDrawn toadd = new ObjectDrawn(whichgraphic * 16 + whichdir * 4 + whichframe, wherex, wherey, wlevel);
        AddObj(toadd);
    }

    public ObjectDrawn GetObj(int heightof, int which) {
        if (heightof < 0) return null;
        if (heightof >= numlevels) return null;
        int howmany = objectlist.get(heightof).size();
        if (which < 0) return null;
        if (which >= howmany) return null;
        return objectlist.get(heightof).get(which);
    }

    public int NumObjsInLevel(int whichlevel) {
        if (objectlist.size() == 0) return 0;
        return objectlist.get(whichlevel).size();
    }
}
