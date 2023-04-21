package nl.huub.van.amelsvoort.duke.tools.map;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import nl.huub.van.amelsvoort.duke.tools.Help;
import nl.huub.van.amelsvoort.duke.tools.Konstanten;

/**
 *
 * @author huub
 * Vertaling van een duke sprite
 */
public class Figuur {

    int x, y, z;

    short cstat, picnum;

    byte shade;

    byte pal, clipdist, filler;

    byte xrepeat, yrepeat;

    byte xoffset, yoffset;

    short sectnum, statnum;

    short ang, owner, xvel, yvel, zvel;

    short lotag, hitag, extra;

    public short getAng() {
        return ang;
    }

    public void setAng(short ang) {
        this.ang = ang;
    }

    public byte getClipdist() {
        return clipdist;
    }

    public void setClipdist(byte clipdist) {
        this.clipdist = clipdist;
    }

    public short getCstat() {
        return cstat;
    }

    public void setCstat(short cstat) {
        this.cstat = cstat;
    }

    public short getExtra() {
        return extra;
    }

    public void setExtra(short extra) {
        this.extra = extra;
    }

    public byte getFiller() {
        return filler;
    }

    public void setFiller(byte filler) {
        this.filler = filler;
    }

    public short getHitag() {
        return hitag;
    }

    public void setHitag(short hitag) {
        this.hitag = hitag;
    }

    public short getLotag() {
        return lotag;
    }

    public void setLotag(short lotag) {
        this.lotag = lotag;
    }

    public short getOwner() {
        return owner;
    }

    public void setOwner(short owner) {
        this.owner = owner;
    }

    public byte getPal() {
        return pal;
    }

    public void setPal(byte pal) {
        this.pal = pal;
    }

    public short getPicnum() {
        return picnum;
    }

    public void setPicnum(short picnum) {
        this.picnum = picnum;
    }

    public short getSectnum() {
        return sectnum;
    }

    public void setSectnum(short sectnum) {
        this.sectnum = sectnum;
    }

    public byte getShade() {
        return shade;
    }

    public void setShade(byte shade) {
        this.shade = shade;
    }

    public short getStatnum() {
        return statnum;
    }

    public void setStatnum(short statnum) {
        this.statnum = statnum;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public byte getXoffset() {
        return xoffset;
    }

    public void setXoffset(byte xoffset) {
        this.xoffset = xoffset;
    }

    public byte getXrepeat() {
        return xrepeat;
    }

    public void setXrepeat(byte xrepeat) {
        this.xrepeat = xrepeat;
    }

    public short getXvel() {
        return xvel;
    }

    public void setXvel(short xvel) {
        this.xvel = xvel;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public byte getYoffset() {
        return yoffset;
    }

    public void setYoffset(byte yoffset) {
        this.yoffset = yoffset;
    }

    public byte getYrepeat() {
        return yrepeat;
    }

    public void setYrepeat(byte yrepeat) {
        this.yrepeat = yrepeat;
    }

    public short getYvel() {
        return yvel;
    }

    public void setYvel(short yvel) {
        this.yvel = yvel;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public short getZvel() {
        return zvel;
    }

    public void setZvel(short zvel) {
        this.zvel = zvel;
    }

    /**
   * Schrijven van de figuren (plaatjes) sprites in Duke naar een bestand
   * @param bestand
   * @param figuren
   * @throws IOException 
   */
    public void schrijvenFiguren(DataOutputStream bestand, ArrayList<Figuur> figuren) throws IOException {
        for (Figuur figuur : figuren) {
            bestand.writeInt(Help.endianInt(figuur.x));
            bestand.writeInt(Help.endianInt(figuur.y));
            bestand.writeInt(Help.endianInt(figuur.z));
            bestand.writeShort(Help.endianShort(figuur.cstat));
            bestand.writeShort(Help.endianShort(figuur.picnum));
            bestand.writeByte(figuur.shade);
            bestand.writeByte(figuur.pal);
            bestand.writeByte(figuur.clipdist);
            bestand.writeByte(figuur.filler);
            bestand.writeByte(figuur.xrepeat);
            bestand.writeByte(figuur.yrepeat);
            bestand.writeByte(figuur.xoffset);
            bestand.writeByte(figuur.yoffset);
            bestand.writeShort(Help.endianShort(figuur.sectnum));
            bestand.writeShort(Help.endianShort(figuur.statnum));
            bestand.writeShort(Help.endianShort(figuur.ang));
            bestand.writeShort(Help.endianShort(figuur.owner));
            bestand.writeShort(Help.endianShort(figuur.xvel));
            bestand.writeShort(Help.endianShort(figuur.yvel));
            bestand.writeShort(Help.endianShort(figuur.zvel));
            bestand.writeShort(Help.endianShort(figuur.lotag));
            bestand.writeShort(Help.endianShort(figuur.hitag));
            bestand.writeShort(Help.endianShort(figuur.extra));
        }
    }

    /**
   * Lezen van een figuur (Sprite)
   * @param in
   * @throws IOException 
   */
    public void leesFiguur(DataInputStream in) throws IOException {
        this.x = Help.endianInt(in.readInt());
        this.y = Help.endianInt(in.readInt());
        this.z = Help.endianInt(in.readInt());
        this.cstat = Help.endianShort(in.readShort());
        this.picnum = Help.endianShort(in.readShort());
        this.shade = in.readByte();
        this.pal = in.readByte();
        this.clipdist = in.readByte();
        this.filler = in.readByte();
        this.xrepeat = in.readByte();
        this.yrepeat = in.readByte();
        this.xoffset = in.readByte();
        this.yoffset = in.readByte();
        this.sectnum = Help.endianShort(in.readShort());
        this.statnum = Help.endianShort(in.readShort());
        this.ang = Help.endianShort(in.readShort());
        this.owner = Help.endianShort(in.readShort());
        this.xvel = Help.endianShort(in.readShort());
        this.yvel = Help.endianShort(in.readShort());
        this.zvel = Help.endianShort(in.readShort());
        this.lotag = Help.endianShort(in.readShort());
        this.hitag = Help.endianShort(in.readShort());
        this.extra = Help.endianShort(in.readShort());
    }

    /**
   * Printen van een figuur record
   * @throws IOException 
   */
    public void print(FileWriter bestand) throws IOException {
        Help.fprintf(bestand, "%s|", Konstanten.TEKST_FIGUUR);
        Help.fprintf(bestand, "x=%d|y=%d|z=%d|", x, y, z);
        Help.fprintf(bestand, "cstat=%d|picnum=%d|", cstat, picnum);
        Help.fprintf(bestand, "shade=%d|", shade);
        Help.fprintf(bestand, "pal=%d|clipdist=%d|filler=%d|", pal, clipdist, filler);
        Help.fprintf(bestand, "xrepeat=%d|yrepeat=%d|", xrepeat, yrepeat);
        Help.fprintf(bestand, "xoffset=%d|yoffset=%d|", xoffset, yoffset);
        Help.fprintf(bestand, "sectnum=%d|statnum=%d|", sectnum, statnum);
        Help.fprintf(bestand, "ang=%d|owner=%d|xvel=%d|yvel=%d|zvel=%d|", ang, owner, xvel, yvel, zvel);
        Help.fprintf(bestand, "lotag=%d|hitag=%d|extra=%d\n", lotag, hitag, extra);
    }

    /**
   * Figuur vullen uit een token
   * @param figuur
   * @param teller
   * @param token1
   * @throws Exception 
   */
    public void verwerkFiguur(Figuur figuur, int teller, String token1) throws Exception {
        int isTeken = token1.indexOf("=");
        String token = token1.substring(isTeken + 1);
        switch(teller) {
            case Konstanten.FIGUUR_ANG:
                figuur.setAng(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_CLIPDIST:
                figuur.setClipdist(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_CSTAT:
                figuur.setCstat(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_EXTRA:
                figuur.setExtra(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_FILLER:
                figuur.setFiller(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_HITAG:
                figuur.setHitag(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_LOTAG:
                figuur.setLotag(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_OWNER:
                figuur.setOwner(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_PAL:
                figuur.setPal(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_PICNUM:
                figuur.setPicnum(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_SECTNUM:
                figuur.setSectnum(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_SHADE:
                figuur.setShade(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_STATNUM:
                figuur.setStatnum(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_X:
                figuur.setX(new Integer(token).intValue());
                break;
            case Konstanten.FIGUUR_XOFFSET:
                figuur.setXoffset(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_XREPEAT:
                figuur.setXrepeat(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_XVEL:
                figuur.setXvel(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_Y:
                figuur.setY(new Integer(token).intValue());
                break;
            case Konstanten.FIGUUR_YOFFSET:
                figuur.setYoffset(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_YREPEAT:
                figuur.setYrepeat(new Byte(token).byteValue());
                break;
            case Konstanten.FIGUUR_YVEL:
                figuur.setYvel(new Short(token).shortValue());
                break;
            case Konstanten.FIGUUR_Z:
                figuur.setZ(new Integer(token).intValue());
                break;
            case Konstanten.FIGUUR_ZVEL:
                figuur.setZvel(new Short(token).shortValue());
                break;
            default:
                String fout = "Fout in figuur token1 : " + token1;
                throw new Exception(fout);
        }
    }
}
