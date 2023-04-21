package mou.net.battle;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import mou.storage.ser.ID;

/**
 * Klasse zum Austauschen der Schiffsinformationen beim K�mpf
 * 
 * @author pb
 */
public final class ShipInfo {

    private int panzerung;

    private int struktur;

    private int waffen;

    private int shild;

    private int shildBackup;

    private int strukturBackup;

    private long idLong;

    private transient ID id;

    /**
	 * Konstruktor f�r deserialize(..) Methode
	 */
    protected ShipInfo() {
        super();
    }

    /**
	 * 
	 */
    public ShipInfo(mou.core.ship.Ship lokaleSchiff) {
        super();
        setStruktur(lokaleSchiff.getStruktur().intValue());
        setIdLong(lokaleSchiff.getID().getVariablePart());
        id = lokaleSchiff.getID();
        setPanzerung((int) lokaleSchiff.getArmor());
        setWaffen((int) lokaleSchiff.getWeapon());
        setShild((int) lokaleSchiff.getShild());
        shildBackup = getShild();
        strukturBackup = getStruktur();
    }

    public ID getId() {
        return id;
    }

    public int getStrukturBackup() {
        return strukturBackup;
    }

    public int getShildBackup() {
        return shildBackup;
    }

    public void setShildBackup(int value) {
        shildBackup = value;
    }

    public long getIdLong() {
        return idLong;
    }

    public void setIdLong(long id) {
        this.idLong = id;
    }

    public int getPanzerung() {
        return panzerung;
    }

    public void setPanzerung(int panzerung) {
        this.panzerung = panzerung;
    }

    public int getShild() {
        return shild;
    }

    public void setShild(int schild) {
        this.shild = schild;
    }

    public int getStruktur() {
        return struktur;
    }

    public void setStruktur(int struktur) {
        this.struktur = struktur;
    }

    public int getWaffen() {
        return waffen;
    }

    public void setWaffen(int waffen) {
        this.waffen = waffen;
    }

    public boolean equals(Object obj) {
        if (obj == null) return false;
        return idLong == ((ShipInfo) obj).idLong;
    }

    public int hashCode() {
        return (int) idLong;
    }

    public static void serialize(mou.core.ship.Ship localShip, DataOutput out) throws IOException {
        out.writeLong(localShip.getID().getVariablePart());
        out.writeInt(localShip.getStruktur().intValue());
        out.writeInt((int) localShip.getArmor());
        out.writeInt((int) localShip.getWeapon());
        out.writeInt((int) localShip.getShild());
    }

    public static ShipInfo deserialize(DataInput in) throws IOException {
        ShipInfo ship = new ShipInfo();
        ship.setIdLong(in.readLong());
        ship.setStruktur(in.readInt());
        ship.setPanzerung(in.readInt());
        ship.setWaffen(in.readInt());
        ship.setShild(in.readInt());
        ship.setShildBackup(ship.shild);
        ship.strukturBackup = ship.struktur;
        return ship;
    }
}
