package lang;

public abstract class Language {

    String date;

    String autoHit;

    String autoCrit;

    String normalHit;

    String criticalHit;

    String dot;

    String heal;

    String dies;

    String beginCast;

    String cast;

    String remove;

    String beginPerform;

    String perform;

    String extraHit;

    String drainGain;

    String gain;

    String afflicted;

    String fades;

    String reflect;

    String miss;

    String spellMiss;

    String dodgeParry;

    String spellDodgeParry;

    String resist;

    String immune1;

    String immune2;

    String crushing;

    public abstract String getAfflicted();

    public abstract String getAutoCrit();

    public abstract String getAutoHit();

    public abstract String getBeginCast();

    public abstract String getBeginPerform();

    public abstract String getCast();

    public abstract String getCriticalHit();

    public abstract String getCrushing();

    public abstract String getDate();

    public abstract String getDies();

    public abstract String getDodgeParry();

    public abstract String getDot();

    public abstract String getDrainGain();

    public abstract String getExtraHit();

    public abstract String getFades();

    public abstract String getGain();

    public abstract String getHeal();

    public abstract String getImmune1();

    public abstract String getImmune2();

    public abstract String getMiss();

    public abstract String getNormalHit();

    public abstract String getPerform();

    public abstract String getReflect();

    public abstract String getRemove();

    public abstract String getResist();

    public abstract String getSpellDodgeParry();

    public abstract String getSpellMiss();
}
