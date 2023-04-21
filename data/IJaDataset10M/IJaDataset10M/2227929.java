package struts2.sample07.mapping;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * 悪魔
 */
@Entity
@Table(name = "devils")
public class Devil {

    /** ID */
    private int id;

    /** 名前 */
    private String name;

    /** 悪魔の種族 */
    private DevilType devilType;

    /** ＨＰ */
    private int hp;

    /** 強さ */
    private int tsuyosa;

    /** 知力 */
    private int chiryoku;

    /** 攻撃 */
    private int kougeki;

    /** 機敏 */
    private int kibin;

    /** 防御 */
    private int bougyo;

    /** 経験値 */
    private int exp;

    /** 魔ッ貨 */
    private int makka;

    /** マグネタイト */
    private int mag;

    @Id
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBougyo() {
        return bougyo;
    }

    public void setBougyo(int bougyo) {
        this.bougyo = bougyo;
    }

    public int getChiryoku() {
        return chiryoku;
    }

    public void setChiryoku(int chiryoku) {
        this.chiryoku = chiryoku;
    }

    @ManyToOne
    public DevilType getDevilType() {
        return devilType;
    }

    public void setDevilType(DevilType devilType) {
        this.devilType = devilType;
    }

    public int getExp() {
        return exp;
    }

    public void setExp(int exp) {
        this.exp = exp;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    public int getKibin() {
        return kibin;
    }

    public void setKibin(int kibin) {
        this.kibin = kibin;
    }

    public int getKougeki() {
        return kougeki;
    }

    public void setKougeki(int kougeki) {
        this.kougeki = kougeki;
    }

    public int getMag() {
        return mag;
    }

    public void setMag(int mag) {
        this.mag = mag;
    }

    public int getMakka() {
        return makka;
    }

    public void setMakka(int makka) {
        this.makka = makka;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTsuyosa() {
        return tsuyosa;
    }

    public void setTsuyosa(int tsuyosa) {
        this.tsuyosa = tsuyosa;
    }
}
