package client_new;

/**
* this class is here to create or to get the meanings of a specified mask
* @author fken
*/
public class MasksManager {

    /**
* there are 2 different gender : male or female
*
* @ return the gender of the character thanks to its charType
* @
*/
    String[] charGenders;

    String[] charRaces;

    String[] charClasses;

    String[] charJobs;

    String[] firstAbilityLvl;

    String[] firstAbilityName;

    String[] secondAbilityLvl;

    String[] secondAbilityName;

    public MasksManager() {
        this.charGenders = new String[2];
        this.charGenders[0] = "female";
        this.charGenders[1] = "male";
        this.charRaces = new String[16];
        this.charRaces[0] = "Human";
        this.charRaces[1] = "Elf";
        this.charRaces[2] = "Dwarf";
        this.charRaces[3] = "Asteth";
        this.charRaces[4] = "Osgohr";
        this.charRaces[5] = "none";
        this.charRaces[6] = "none";
        this.charRaces[7] = "none";
        this.charRaces[8] = "none";
        this.charRaces[9] = "none";
        this.charRaces[10] = "none";
        this.charRaces[11] = "none";
        this.charRaces[12] = "none";
        this.charRaces[13] = "none";
        this.charRaces[14] = "none";
        this.charRaces[15] = "none";
        this.charClasses = new String[32];
        this.charClasses[0] = "class1";
        this.charClasses[1] = "class2";
        this.charClasses[2] = "class3";
        this.charClasses[3] = "class4";
        this.charClasses[4] = "class5";
        this.charClasses[5] = "class6";
        this.charClasses[6] = "class7";
        this.charClasses[7] = "class8";
        this.charClasses[8] = "class9";
        this.charClasses[9] = "class10";
        this.charClasses[10] = "class11";
        this.charClasses[11] = "class12";
        this.charClasses[12] = "class13";
        this.charClasses[13] = "class14";
        this.charClasses[14] = "class15";
        this.charClasses[15] = "class16";
        this.charClasses[16] = "class17";
        this.charClasses[17] = "class18";
        this.charClasses[18] = "class19";
        this.charClasses[19] = "class20";
        this.charClasses[20] = "class21";
        this.charClasses[21] = "class22";
        this.charClasses[22] = "class23";
        this.charClasses[23] = "class24";
        this.charClasses[24] = "class25";
        this.charClasses[25] = "class26";
        this.charClasses[26] = "class27";
        this.charClasses[27] = "class28";
        this.charClasses[28] = "class29";
        this.charClasses[29] = "class30";
        this.charClasses[30] = "class31";
        this.charClasses[31] = "class32";
        this.charJobs = new String[64];
        this.charJobs[0] = "job1";
        this.charJobs[1] = "job2";
        this.charJobs[2] = "job3";
        this.charJobs[3] = "job4";
        this.charJobs[4] = "job5";
        this.charJobs[5] = "job6";
        this.charJobs[6] = "job7";
        this.charJobs[7] = "job8";
        this.charJobs[8] = "job9";
        this.charJobs[9] = "job10";
        this.charJobs[10] = "job11";
        this.charJobs[11] = "job12";
        this.charJobs[12] = "job13";
        this.charJobs[13] = "job14";
        this.charJobs[14] = "job15";
        this.charJobs[15] = "job16";
        this.charJobs[16] = "job17";
        this.charJobs[17] = "job18";
        this.charJobs[18] = "job19";
        this.charJobs[19] = "job20";
        this.charJobs[20] = "job21";
        this.charJobs[21] = "job22";
        this.charJobs[22] = "job23";
        this.charJobs[23] = "job24";
        this.charJobs[24] = "job25";
        this.charJobs[25] = "job26";
        this.charJobs[26] = "job27";
        this.charJobs[27] = "job28";
        this.charJobs[28] = "job29";
        this.charJobs[29] = "job30";
        this.charJobs[30] = "job31";
        this.charJobs[31] = "job32";
        this.charJobs[32] = "job33";
        this.charJobs[33] = "job34";
        this.charJobs[34] = "job35";
        this.charJobs[35] = "job36";
        this.charJobs[36] = "job37";
        this.charJobs[37] = "job38";
        this.charJobs[38] = "job39";
        this.charJobs[39] = "job40";
        this.charJobs[40] = "job41";
        this.charJobs[41] = "job42";
        this.charJobs[42] = "job43";
        this.charJobs[43] = "job44";
        this.charJobs[44] = "job45";
        this.charJobs[45] = "job46";
        this.charJobs[46] = "job47";
        this.charJobs[47] = "job48";
        this.charJobs[48] = "job49";
        this.charJobs[49] = "job50";
        this.charJobs[50] = "job51";
        this.charJobs[51] = "job52";
        this.charJobs[52] = "job53";
        this.charJobs[53] = "job54";
        this.charJobs[54] = "job55";
        this.charJobs[55] = "job56";
        this.charJobs[56] = "job57";
        this.charJobs[57] = "job58";
        this.charJobs[58] = "job59";
        this.charJobs[59] = "job60";
        this.charJobs[60] = "job61";
        this.charJobs[61] = "job62";
        this.charJobs[62] = "job63";
        this.charJobs[63] = "job64";
        this.firstAbilityLvl = new String[32];
        this.firstAbilityLvl[0] = "fAbility1";
        this.firstAbilityLvl[1] = "fAbility2";
        this.firstAbilityLvl[2] = "fAbility3";
        this.firstAbilityLvl[3] = "fAbility4";
        this.firstAbilityLvl[4] = "fAbility5";
        this.firstAbilityLvl[5] = "fAbility6";
        this.firstAbilityLvl[6] = "fAbility7";
        this.firstAbilityLvl[7] = "fAbility8";
        this.firstAbilityLvl[8] = "fAbility9";
        this.firstAbilityLvl[9] = "fAbility10";
        this.firstAbilityLvl[10] = "fAbility11";
        this.firstAbilityLvl[11] = "fAbility12";
        this.firstAbilityLvl[12] = "fAbility13";
        this.firstAbilityLvl[13] = "fAbility14";
        this.firstAbilityLvl[14] = "fAbility15";
        this.firstAbilityLvl[15] = "fAbility16";
        this.firstAbilityLvl[16] = "fAbility17";
        this.firstAbilityLvl[17] = "fAbility18";
        this.firstAbilityLvl[18] = "fAbility19";
        this.firstAbilityLvl[19] = "fAbility20";
        this.firstAbilityLvl[20] = "fAbility21";
        this.firstAbilityLvl[21] = "fAbility22";
        this.firstAbilityLvl[22] = "fAbility23";
        this.firstAbilityLvl[23] = "fAbility24";
        this.firstAbilityLvl[24] = "fAbility25";
        this.firstAbilityLvl[25] = "fAbility26";
        this.firstAbilityLvl[26] = "fAbility27";
        this.firstAbilityLvl[27] = "fAbility28";
        this.firstAbilityLvl[28] = "fAbility29";
        this.firstAbilityLvl[29] = "fAbility30";
        this.firstAbilityLvl[30] = "fAbility31";
        this.firstAbilityLvl[31] = "fAbility32";
        this.secondAbilityLvl = new String[32];
        this.secondAbilityLvl[0] = "sAbility1";
        this.secondAbilityLvl[1] = "sAbility2";
        this.secondAbilityLvl[2] = "sAbility3";
        this.secondAbilityLvl[3] = "sAbility4";
        this.secondAbilityLvl[4] = "sAbility5";
        this.secondAbilityLvl[5] = "sAbility6";
        this.secondAbilityLvl[6] = "sAbility7";
        this.secondAbilityLvl[7] = "sAbility8";
        this.secondAbilityLvl[8] = "sAbility9";
        this.secondAbilityLvl[9] = "sAbility10";
        this.secondAbilityLvl[10] = "sAbility11";
        this.secondAbilityLvl[11] = "sAbility12";
        this.secondAbilityLvl[12] = "sAbility13";
        this.secondAbilityLvl[13] = "sAbility14";
        this.secondAbilityLvl[14] = "sAbility15";
        this.secondAbilityLvl[15] = "sAbility16";
        this.secondAbilityLvl[16] = "fAbility17";
        this.secondAbilityLvl[17] = "fAbility18";
        this.secondAbilityLvl[18] = "fAbility19";
        this.secondAbilityLvl[19] = "fAbility20";
        this.secondAbilityLvl[20] = "fAbility21";
        this.secondAbilityLvl[21] = "fAbility22";
        this.secondAbilityLvl[22] = "fAbility23";
        this.secondAbilityLvl[23] = "fAbility24";
        this.secondAbilityLvl[24] = "fAbility25";
        this.secondAbilityLvl[25] = "fAbility26";
        this.secondAbilityLvl[26] = "fAbility27";
        this.secondAbilityLvl[27] = "fAbility28";
        this.secondAbilityLvl[28] = "fAbility29";
        this.secondAbilityLvl[29] = "fAbility30";
        this.secondAbilityLvl[30] = "fAbility31";
        this.secondAbilityLvl[31] = "fAbility32";
        this.firstAbilityName = new String[8];
        this.firstAbilityName[0] = "Name1";
        this.firstAbilityName[1] = "Name2";
        this.firstAbilityName[2] = "Name3";
        this.firstAbilityName[3] = "Name4";
        this.firstAbilityName[4] = "Name5";
        this.firstAbilityName[5] = "Name6";
        this.firstAbilityName[6] = "Name7";
        this.firstAbilityName[7] = "Name8";
        this.secondAbilityName = new String[8];
        this.secondAbilityName[0] = "Name1";
        this.secondAbilityName[1] = "Name2";
        this.secondAbilityName[2] = "Name3";
        this.secondAbilityName[3] = "Name4";
        this.secondAbilityName[4] = "Name5";
        this.secondAbilityName[5] = "Name6";
        this.secondAbilityName[6] = "Name7";
        this.secondAbilityName[7] = "Name8";
    }

    /**
* a way to get meaning of a the masked value hidden in a short integer
*
* @author fken
* @return a string which explains the meaning of the masked value
* @param table the meaning list where you can find the signification of the masked value
* @param shortValue the masked value we need to unmask
* @param begin first bit of the mask
* @param end last bit of the mask
*/
    private String getShortMeaning(String[] table, short shortValue, int begin, int end) {
        int position;
        short mask = 0;
        for (int i = begin; i < end; ++i) mask += 1 << i;
        int unmask = (shortValue & mask);
        position = ((unmask < 0) ? -unmask : unmask) / (1 << begin);
        return table[position];
    }

    /**
* a way to get meaning (gender) of a the masked value hidden in a short integer
*
* @author fken
* @return the gender of the character
* @param charType the masked value
*/
    public String getGender(short charType) {
        return getShortMeaning(charGenders, charType, 15, 16);
    }

    /**
* a way to get meaning (race) of a the masked value hidden in a short integer
*
* @author fken
* @return the race of the character
* @param charType the masked value
*/
    public String getRace(short charType) {
        return getShortMeaning(charRaces, charType, 11, 15);
    }

    /**
* a way to get meaning (class) of a the masked value hidden in a short integer
*
* @author fken
* @return the class of the character
* @param charType the masked value
*/
    public String getClass(short charType) {
        return getShortMeaning(charClasses, charType, 6, 11);
    }

    /**
* a way to get meaning (job) of a the masked value hidden in a short integer
*
* @author fken
* @return the job of the character
* @param charType the masked value
*/
    public String getJob(short charType) {
        return getShortMeaning(charJobs, charType, 0, 6);
    }

    /**
* a way to get meaning (first ability level) of a the masked value hidden in a short integer
*
* @author fken
* @return the first ability level of the character
* @param classAbilities the masked value
*/
    public String getFirstAbilityLvl(short classAbilities) {
        return getShortMeaning(firstAbilityLvl, classAbilities, 11, 16);
    }

    /**
* a way to get meaning (first ability name) of a the masked value hidden in a short integer
*
* @author fken
* @return the first ability name of the character
* @param classAbilities the masked value
*/
    public String getFirstAbilityName(short classAbilities) {
        return getShortMeaning(firstAbilityName, classAbilities, 8, 11);
    }

    /**
* a way to get meaning (second ability level) of a the masked value hidden in a short integer
*
* @author fken
* @return the second ability level of the character
* @param classAbilities the masked value
*/
    public String getSecondAbilityLvl(short classAbilities) {
        return getShortMeaning(secondAbilityLvl, classAbilities, 3, 8);
    }

    /**
* a way to get meaning (second ability name) of a the masked value hidden in a short integer
*
* @author fken
* @return the second ability name of the character
* @param classAbilities the masked value
*/
    public String getSecondAbilityName(short classAbilities) {
        return getShortMeaning(secondAbilityName, classAbilities, 0, 3);
    }
}
