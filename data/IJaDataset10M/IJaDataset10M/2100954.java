package csa.jportal.gameModes.player;

import csa.util.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;

public class PlayerData {

    protected String mClass = "";

    public String mName = "";

    protected String mPlayerName = "";

    protected String mHeapName = "";

    protected int mMoney = 0;

    protected int mGamesPlayed = 0;

    protected int mWins = 0;

    protected int mMoneySpend = 0;

    protected Vector<String> mQuestsCompleted = new Vector<String>();

    protected Vector<String> mQuestsOpen = new Vector<String>();

    protected Vector<String> mNamedAIOpen = new Vector<String>();

    protected Vector<String> mSetBosterUnOpened = new Vector<String>();

    protected Vector<String> mSetBoosterOpened = new Vector<String>();

    protected Vector<String> mKnownSets = new Vector<String>();

    protected int mLoses = 0;

    protected int mDraws = 0;

    protected Vector<String> mQuestsActive = new Vector<String>();

    protected Vector<String> mQuestDataStorage = new Vector<String>();

    protected Vector<String> mSpecialOfferItem = new Vector<String>();

    protected Vector<Integer> mSpecialOfferItemPrice = new Vector<Integer>();

    protected String mLastVisitDate = "";

    protected int mBoostersBought = 0;

    protected Vector<String> mAchievmentsReached = new Vector<String>();

    protected int mHPModifier = 0;

    protected int mBoosterDraft1Played = 0;

    protected int mBoosterDraft1Wins = 0;

    protected int mBoosterDraft2Played = 0;

    protected int mBoosterDraft2Win = 0;

    protected int mBoosterDraft3Played = 0;

    protected int mBoosterDraft3Win = 0;

    protected int mBoosterDraft4Played = 0;

    protected int mBoosterDraft4Win = 0;

    protected int mBoosterDraft5Played = 0;

    protected int mBoosterDraft5Win = 0;

    protected int mBoosterDraft6Played = 0;

    protected int mBoosterDraft6Win = 0;

    protected int mBoosterDraft7Played = 0;

    protected int mBoosterDraft7Win = 0;

    protected int mBoosterDraft8Played = 0;

    protected int mBoosterDraft8Win = 0;

    protected int mFreeMulligan = 0;

    protected int mNumberChoiceStartCards = 0;

    protected int mOpponnetHPModifier = 0;

    protected int mHPAbsorbArmour = 0;

    protected int mPlayerDamageAdd = 0;

    protected int mCardDamageAdd = 0;

    protected int mRandomStartLandCount = 0;

    protected int mHighiestDamageDealt1Round = 0;

    protected int mHighiestDamageReceived1Round = 0;

    protected int mHighiestDamageReceived1RoundSurvived = 0;

    protected int mAdditionalGoldPerVictory = 0;

    protected int mAdditionalGoldPerSellPercent = 0;

    protected int mPriceCheapeningPercentWhite = 0;

    protected int mPriceCheapeningPercentGreen = 0;

    protected int mPriceCheapeningPercentBlack = 0;

    protected int mPriceCheapeningPercentBlue = 0;

    protected int mPriceCheapeningPercentRed = 0;

    protected int mBoostersGot = 0;

    protected int mMaxHPInGame = 0;

    protected int mMaxDamageDealtToOtherPlayer = 0;

    protected int mMaxDamageSurvived = 0;

    protected int mCreatureMax = 0;

    protected String mAvatarImage = "";

    public String getPlayerName() {
        return mPlayerName;
    }

    public void setPlayerName(String PlayerName) {
        mPlayerName = PlayerName;
    }

    public String getHeapName() {
        return mHeapName;
    }

    public void setHeapName(String HeapName) {
        mHeapName = HeapName;
    }

    public int getMoney() {
        return mMoney;
    }

    public void setMoney(int Money) {
        mMoney = Money;
    }

    public int getGamesPlayed() {
        return mGamesPlayed;
    }

    public void setGamesPlayed(int GamesPlayed) {
        mGamesPlayed = GamesPlayed;
    }

    public int getWins() {
        return mWins;
    }

    public void setWins(int Wins) {
        mWins = Wins;
    }

    public int getMoneySpend() {
        return mMoneySpend;
    }

    public void setMoneySpend(int MoneySpend) {
        mMoneySpend = MoneySpend;
    }

    public Vector<String> getQuestsCompleted() {
        return mQuestsCompleted;
    }

    public void setQuestsCompleted(Vector<String> QuestsCompleted) {
        mQuestsCompleted = QuestsCompleted;
    }

    public Vector<String> getQuestsOpen() {
        return mQuestsOpen;
    }

    public void setQuestsOpen(Vector<String> QuestsOpen) {
        mQuestsOpen = QuestsOpen;
    }

    public Vector<String> getNamedAIOpen() {
        return mNamedAIOpen;
    }

    public void setNamedAIOpen(Vector<String> NamedAIOpen) {
        mNamedAIOpen = NamedAIOpen;
    }

    public Vector<String> getSetBosterUnOpened() {
        return mSetBosterUnOpened;
    }

    public void setSetBosterUnOpened(Vector<String> SetBosterUnOpened) {
        mSetBosterUnOpened = SetBosterUnOpened;
    }

    public Vector<String> getSetBoosterOpened() {
        return mSetBoosterOpened;
    }

    public void setSetBoosterOpened(Vector<String> SetBoosterOpened) {
        mSetBoosterOpened = SetBoosterOpened;
    }

    public Vector<String> getKnownSets() {
        return mKnownSets;
    }

    public void setKnownSets(Vector<String> KnownSets) {
        mKnownSets = KnownSets;
    }

    public int getLoses() {
        return mLoses;
    }

    public void setLoses(int Loses) {
        mLoses = Loses;
    }

    public int getDraws() {
        return mDraws;
    }

    public void setDraws(int Draws) {
        mDraws = Draws;
    }

    public Vector<String> getQuestsActive() {
        return mQuestsActive;
    }

    public void setQuestsActive(Vector<String> QuestsActive) {
        mQuestsActive = QuestsActive;
    }

    public Vector<String> getQuestDataStorage() {
        return mQuestDataStorage;
    }

    public void setQuestDataStorage(Vector<String> QuestDataStorage) {
        mQuestDataStorage = QuestDataStorage;
    }

    public Vector<String> getSpecialOfferItem() {
        return mSpecialOfferItem;
    }

    public void setSpecialOfferItem(Vector<String> SpecialOfferItem) {
        mSpecialOfferItem = SpecialOfferItem;
    }

    public Vector<Integer> getSpecialOfferItemPrice() {
        return mSpecialOfferItemPrice;
    }

    public void setSpecialOfferItemPrice(Vector<Integer> SpecialOfferItemPrice) {
        mSpecialOfferItemPrice = SpecialOfferItemPrice;
    }

    public String getLastVisitDate() {
        return mLastVisitDate;
    }

    public void setLastVisitDate(String LastVisitDate) {
        mLastVisitDate = LastVisitDate;
    }

    public int getBoostersBought() {
        return mBoostersBought;
    }

    public void setBoostersBought(int BoostersBought) {
        mBoostersBought = BoostersBought;
    }

    public Vector<String> getAchievmentsReached() {
        return mAchievmentsReached;
    }

    public void setAchievmentsReached(Vector<String> AchievmentsReached) {
        mAchievmentsReached = AchievmentsReached;
    }

    public int getHPModifier() {
        return mHPModifier;
    }

    public void setHPModifier(int HPModifier) {
        mHPModifier = HPModifier;
    }

    public int getBoosterDraft1Played() {
        return mBoosterDraft1Played;
    }

    public void setBoosterDraft1Played(int BoosterDraft1Played) {
        mBoosterDraft1Played = BoosterDraft1Played;
    }

    public int getBoosterDraft1Wins() {
        return mBoosterDraft1Wins;
    }

    public void setBoosterDraft1Wins(int BoosterDraft1Wins) {
        mBoosterDraft1Wins = BoosterDraft1Wins;
    }

    public int getBoosterDraft2Played() {
        return mBoosterDraft2Played;
    }

    public void setBoosterDraft2Played(int BoosterDraft2Played) {
        mBoosterDraft2Played = BoosterDraft2Played;
    }

    public int getBoosterDraft2Win() {
        return mBoosterDraft2Win;
    }

    public void setBoosterDraft2Win(int BoosterDraft2Win) {
        mBoosterDraft2Win = BoosterDraft2Win;
    }

    public int getBoosterDraft3Played() {
        return mBoosterDraft3Played;
    }

    public void setBoosterDraft3Played(int BoosterDraft3Played) {
        mBoosterDraft3Played = BoosterDraft3Played;
    }

    public int getBoosterDraft3Win() {
        return mBoosterDraft3Win;
    }

    public void setBoosterDraft3Win(int BoosterDraft3Win) {
        mBoosterDraft3Win = BoosterDraft3Win;
    }

    public int getBoosterDraft4Played() {
        return mBoosterDraft4Played;
    }

    public void setBoosterDraft4Played(int BoosterDraft4Played) {
        mBoosterDraft4Played = BoosterDraft4Played;
    }

    public int getBoosterDraft4Win() {
        return mBoosterDraft4Win;
    }

    public void setBoosterDraft4Win(int BoosterDraft4Win) {
        mBoosterDraft4Win = BoosterDraft4Win;
    }

    public int getBoosterDraft5Played() {
        return mBoosterDraft5Played;
    }

    public void setBoosterDraft5Played(int BoosterDraft5Played) {
        mBoosterDraft5Played = BoosterDraft5Played;
    }

    public int getBoosterDraft5Win() {
        return mBoosterDraft5Win;
    }

    public void setBoosterDraft5Win(int BoosterDraft5Win) {
        mBoosterDraft5Win = BoosterDraft5Win;
    }

    public int getBoosterDraft6Played() {
        return mBoosterDraft6Played;
    }

    public void setBoosterDraft6Played(int BoosterDraft6Played) {
        mBoosterDraft6Played = BoosterDraft6Played;
    }

    public int getBoosterDraft6Win() {
        return mBoosterDraft6Win;
    }

    public void setBoosterDraft6Win(int BoosterDraft6Win) {
        mBoosterDraft6Win = BoosterDraft6Win;
    }

    public int getBoosterDraft7Played() {
        return mBoosterDraft7Played;
    }

    public void setBoosterDraft7Played(int BoosterDraft7Played) {
        mBoosterDraft7Played = BoosterDraft7Played;
    }

    public int getBoosterDraft7Win() {
        return mBoosterDraft7Win;
    }

    public void setBoosterDraft7Win(int BoosterDraft7Win) {
        mBoosterDraft7Win = BoosterDraft7Win;
    }

    public int getBoosterDraft8Played() {
        return mBoosterDraft8Played;
    }

    public void setBoosterDraft8Played(int BoosterDraft8Played) {
        mBoosterDraft8Played = BoosterDraft8Played;
    }

    public int getBoosterDraft8Win() {
        return mBoosterDraft8Win;
    }

    public void setBoosterDraft8Win(int BoosterDraft8Win) {
        mBoosterDraft8Win = BoosterDraft8Win;
    }

    public int getFreeMulligan() {
        return mFreeMulligan;
    }

    public void setFreeMulligan(int FreeMulligan) {
        mFreeMulligan = FreeMulligan;
    }

    public int getNumberChoiceStartCards() {
        return mNumberChoiceStartCards;
    }

    public void setNumberChoiceStartCards(int NumberChoiceStartCards) {
        mNumberChoiceStartCards = NumberChoiceStartCards;
    }

    public int getOpponnetHPModifier() {
        return mOpponnetHPModifier;
    }

    public void setOpponnetHPModifier(int OpponnetHPModifier) {
        mOpponnetHPModifier = OpponnetHPModifier;
    }

    public int getHPAbsorbArmour() {
        return mHPAbsorbArmour;
    }

    public void setHPAbsorbArmour(int HPAbsorbArmour) {
        mHPAbsorbArmour = HPAbsorbArmour;
    }

    public int getPlayerDamageAdd() {
        return mPlayerDamageAdd;
    }

    public void setPlayerDamageAdd(int PlayerDamageAdd) {
        mPlayerDamageAdd = PlayerDamageAdd;
    }

    public int getCardDamageAdd() {
        return mCardDamageAdd;
    }

    public void setCardDamageAdd(int CardDamageAdd) {
        mCardDamageAdd = CardDamageAdd;
    }

    public int getRandomStartLandCount() {
        return mRandomStartLandCount;
    }

    public void setRandomStartLandCount(int RandomStartLandCount) {
        mRandomStartLandCount = RandomStartLandCount;
    }

    public int getHighiestDamageDealt1Round() {
        return mHighiestDamageDealt1Round;
    }

    public void setHighiestDamageDealt1Round(int HighiestDamageDealt1Round) {
        mHighiestDamageDealt1Round = HighiestDamageDealt1Round;
    }

    public int getHighiestDamageReceived1Round() {
        return mHighiestDamageReceived1Round;
    }

    public void setHighiestDamageReceived1Round(int HighiestDamageReceived1Round) {
        mHighiestDamageReceived1Round = HighiestDamageReceived1Round;
    }

    public int getHighiestDamageReceived1RoundSurvived() {
        return mHighiestDamageReceived1RoundSurvived;
    }

    public void setHighiestDamageReceived1RoundSurvived(int HighiestDamageReceived1RoundSurvived) {
        mHighiestDamageReceived1RoundSurvived = HighiestDamageReceived1RoundSurvived;
    }

    public int getAdditionalGoldPerVictory() {
        return mAdditionalGoldPerVictory;
    }

    public void setAdditionalGoldPerVictory(int AdditionalGoldPerVictory) {
        mAdditionalGoldPerVictory = AdditionalGoldPerVictory;
    }

    public int getAdditionalGoldPerSellPercent() {
        return mAdditionalGoldPerSellPercent;
    }

    public void setAdditionalGoldPerSellPercent(int AdditionalGoldPerSellPercent) {
        mAdditionalGoldPerSellPercent = AdditionalGoldPerSellPercent;
    }

    public int getPriceCheapeningPercentWhite() {
        return mPriceCheapeningPercentWhite;
    }

    public void setPriceCheapeningPercentWhite(int PriceCheapeningPercentWhite) {
        mPriceCheapeningPercentWhite = PriceCheapeningPercentWhite;
    }

    public int getPriceCheapeningPercentGreen() {
        return mPriceCheapeningPercentGreen;
    }

    public void setPriceCheapeningPercentGreen(int PriceCheapeningPercentGreen) {
        mPriceCheapeningPercentGreen = PriceCheapeningPercentGreen;
    }

    public int getPriceCheapeningPercentBlack() {
        return mPriceCheapeningPercentBlack;
    }

    public void setPriceCheapeningPercentBlack(int PriceCheapeningPercentBlack) {
        mPriceCheapeningPercentBlack = PriceCheapeningPercentBlack;
    }

    public int getPriceCheapeningPercentBlue() {
        return mPriceCheapeningPercentBlue;
    }

    public void setPriceCheapeningPercentBlue(int PriceCheapeningPercentBlue) {
        mPriceCheapeningPercentBlue = PriceCheapeningPercentBlue;
    }

    public int getPriceCheapeningPercentRed() {
        return mPriceCheapeningPercentRed;
    }

    public void setPriceCheapeningPercentRed(int PriceCheapeningPercentRed) {
        mPriceCheapeningPercentRed = PriceCheapeningPercentRed;
    }

    public int getBoostersGot() {
        return mBoostersGot;
    }

    public void setBoostersGot(int BoostersGot) {
        mBoostersGot = BoostersGot;
    }

    public int getMaxHPInGame() {
        return mMaxHPInGame;
    }

    public void setMaxHPInGame(int MaxHPInGame) {
        mMaxHPInGame = MaxHPInGame;
    }

    public int getMaxDamageDealtToOtherPlayer() {
        return mMaxDamageDealtToOtherPlayer;
    }

    public void setMaxDamageDealtToOtherPlayer(int MaxDamageDealtToOtherPlayer) {
        mMaxDamageDealtToOtherPlayer = MaxDamageDealtToOtherPlayer;
    }

    public int getMaxDamageSurvived() {
        return mMaxDamageSurvived;
    }

    public void setMaxDamageSurvived(int MaxDamageSurvived) {
        mMaxDamageSurvived = MaxDamageSurvived;
    }

    public int getCreatureMax() {
        return mCreatureMax;
    }

    public void setCreatureMax(int CreatureMax) {
        mCreatureMax = CreatureMax;
    }

    public String getAvatarImage() {
        return mAvatarImage;
    }

    public void setAvatarImage(String AvatarImage) {
        mAvatarImage = AvatarImage;
    }

    private String exportXML() {
        String s = new String();
        s += "\t<PlayerData>\n";
        s += "\t\t<Class>" + UtilityString.toXML(mClass) + "</Class>\n";
        s += "\t\t<Name>" + UtilityString.toXML(mName) + "</Name>\n";
        s += "\t\t<PLAYERNAME>" + UtilityString.toXML(mPlayerName) + "</PLAYERNAME>\n";
        s += "\t\t<HEAPNAME>" + UtilityString.toXML(mHeapName) + "</HEAPNAME>\n";
        s += "\t\t<MONEY>" + mMoney + "</MONEY>\n";
        s += "\t\t<GamesPlayed>" + mGamesPlayed + "</GamesPlayed>\n";
        s += "\t\t<WINS>" + mWins + "</WINS>\n";
        s += "\t\t<MoneySpend>" + mMoneySpend + "</MoneySpend>\n";
        s += "\t\t<QUESTSCOMPLETEDs>\n";
        for (int i = 0; i < mQuestsCompleted.size(); i++) {
            s += "\t\t\t<QUESTSCOMPLETED>" + UtilityString.toXML(mQuestsCompleted.elementAt(i)) + "</QUESTSCOMPLETED>\n";
        }
        s += "\t\t</QUESTSCOMPLETEDs>\n";
        s += "\t\t<QUESTSOPENs>\n";
        for (int i = 0; i < mQuestsOpen.size(); i++) {
            s += "\t\t\t<QUESTSOPEN>" + UtilityString.toXML(mQuestsOpen.elementAt(i)) + "</QUESTSOPEN>\n";
        }
        s += "\t\t</QUESTSOPENs>\n";
        s += "\t\t<NAMEDAIOPENs>\n";
        for (int i = 0; i < mNamedAIOpen.size(); i++) {
            s += "\t\t\t<NAMEDAIOPEN>" + UtilityString.toXML(mNamedAIOpen.elementAt(i)) + "</NAMEDAIOPEN>\n";
        }
        s += "\t\t</NAMEDAIOPENs>\n";
        s += "\t\t<SETBOOSTERUNOPNEDs>\n";
        for (int i = 0; i < mSetBosterUnOpened.size(); i++) {
            s += "\t\t\t<SETBOOSTERUNOPNED>" + UtilityString.toXML(mSetBosterUnOpened.elementAt(i)) + "</SETBOOSTERUNOPNED>\n";
        }
        s += "\t\t</SETBOOSTERUNOPNEDs>\n";
        s += "\t\t<SETBOOOSTEROPENEDs>\n";
        for (int i = 0; i < mSetBoosterOpened.size(); i++) {
            s += "\t\t\t<SETBOOOSTEROPENED>" + UtilityString.toXML(mSetBoosterOpened.elementAt(i)) + "</SETBOOOSTEROPENED>\n";
        }
        s += "\t\t</SETBOOOSTEROPENEDs>\n";
        s += "\t\t<KNOWNSETSs>\n";
        for (int i = 0; i < mKnownSets.size(); i++) {
            s += "\t\t\t<KNOWNSETS>" + UtilityString.toXML(mKnownSets.elementAt(i)) + "</KNOWNSETS>\n";
        }
        s += "\t\t</KNOWNSETSs>\n";
        s += "\t\t<LOSES>" + mLoses + "</LOSES>\n";
        s += "\t\t<DRAWS>" + mDraws + "</DRAWS>\n";
        s += "\t\t<QUESTSACTIVEs>\n";
        for (int i = 0; i < mQuestsActive.size(); i++) {
            s += "\t\t\t<QUESTSACTIVE>" + UtilityString.toXML(mQuestsActive.elementAt(i)) + "</QUESTSACTIVE>\n";
        }
        s += "\t\t</QUESTSACTIVEs>\n";
        s += "\t\t<QUESTDATASTORAGEs>\n";
        for (int i = 0; i < mQuestDataStorage.size(); i++) {
            s += "\t\t\t<QUESTDATASTORAGE>" + UtilityString.toXML(mQuestDataStorage.elementAt(i)) + "</QUESTDATASTORAGE>\n";
        }
        s += "\t\t</QUESTDATASTORAGEs>\n";
        s += "\t\t<SEPCIALOFFERITEMs>\n";
        for (int i = 0; i < mSpecialOfferItem.size(); i++) {
            s += "\t\t\t<SEPCIALOFFERITEM>" + UtilityString.toXML(mSpecialOfferItem.elementAt(i)) + "</SEPCIALOFFERITEM>\n";
        }
        s += "\t\t</SEPCIALOFFERITEMs>\n";
        s += "\t\t<SPECIALOFFERITEMPRICEs>\n";
        for (int i = 0; i < mSpecialOfferItemPrice.size(); i++) {
            s += "\t\t\t<SPECIALOFFERITEMPRICE>" + mSpecialOfferItemPrice.elementAt(i) + "</SPECIALOFFERITEMPRICE>\n";
        }
        s += "\t\t</SPECIALOFFERITEMPRICEs>\n";
        s += "\t\t<LASTVISITDATE>" + UtilityString.toXML(mLastVisitDate) + "</LASTVISITDATE>\n";
        s += "\t\t<BOOSTERSBOUGHT>" + mBoostersBought + "</BOOSTERSBOUGHT>\n";
        s += "\t\t<ACHIEVMENTS_REACHEDs>\n";
        for (int i = 0; i < mAchievmentsReached.size(); i++) {
            s += "\t\t\t<ACHIEVMENTS_REACHED>" + UtilityString.toXML(mAchievmentsReached.elementAt(i)) + "</ACHIEVMENTS_REACHED>\n";
        }
        s += "\t\t</ACHIEVMENTS_REACHEDs>\n";
        s += "\t\t<HPMODIFIER>" + mHPModifier + "</HPMODIFIER>\n";
        s += "\t\t<BoosterDraft1Played>" + mBoosterDraft1Played + "</BoosterDraft1Played>\n";
        s += "\t\t<BoosterDraft1Wins>" + mBoosterDraft1Wins + "</BoosterDraft1Wins>\n";
        s += "\t\t<BoosterDraft2Played>" + mBoosterDraft2Played + "</BoosterDraft2Played>\n";
        s += "\t\t<BoosterDraft2Win>" + mBoosterDraft2Win + "</BoosterDraft2Win>\n";
        s += "\t\t<BoosterDraft3Played>" + mBoosterDraft3Played + "</BoosterDraft3Played>\n";
        s += "\t\t<BoosterDraft3Win>" + mBoosterDraft3Win + "</BoosterDraft3Win>\n";
        s += "\t\t<BoosterDraft4Played>" + mBoosterDraft4Played + "</BoosterDraft4Played>\n";
        s += "\t\t<BoosterDraft4Win>" + mBoosterDraft4Win + "</BoosterDraft4Win>\n";
        s += "\t\t<BoosterDraft5Played>" + mBoosterDraft5Played + "</BoosterDraft5Played>\n";
        s += "\t\t<BoosterDraft5Win>" + mBoosterDraft5Win + "</BoosterDraft5Win>\n";
        s += "\t\t<BoosterDraft6Played>" + mBoosterDraft6Played + "</BoosterDraft6Played>\n";
        s += "\t\t<BoosterDraft6Win>" + mBoosterDraft6Win + "</BoosterDraft6Win>\n";
        s += "\t\t<BoosterDraft7Played>" + mBoosterDraft7Played + "</BoosterDraft7Played>\n";
        s += "\t\t<BoosterDraft7Win>" + mBoosterDraft7Win + "</BoosterDraft7Win>\n";
        s += "\t\t<BoosterDraft8Played>" + mBoosterDraft8Played + "</BoosterDraft8Played>\n";
        s += "\t\t<BoosterDraft8Win>" + mBoosterDraft8Win + "</BoosterDraft8Win>\n";
        s += "\t\t<FreeMulligan>" + mFreeMulligan + "</FreeMulligan>\n";
        s += "\t\t<NumberChoiceStartCards>" + mNumberChoiceStartCards + "</NumberChoiceStartCards>\n";
        s += "\t\t<OpponnetHPModifier>" + mOpponnetHPModifier + "</OpponnetHPModifier>\n";
        s += "\t\t<HPAbsorbArmour>" + mHPAbsorbArmour + "</HPAbsorbArmour>\n";
        s += "\t\t<PlayerDamageAdd>" + mPlayerDamageAdd + "</PlayerDamageAdd>\n";
        s += "\t\t<CardDamageAdd>" + mCardDamageAdd + "</CardDamageAdd>\n";
        s += "\t\t<RandomStartLandCount>" + mRandomStartLandCount + "</RandomStartLandCount>\n";
        s += "\t\t<HighiestDamageDealt1Round>" + mHighiestDamageDealt1Round + "</HighiestDamageDealt1Round>\n";
        s += "\t\t<HighiestDamageReceived1Round>" + mHighiestDamageReceived1Round + "</HighiestDamageReceived1Round>\n";
        s += "\t\t<HighiestDamageReceived1RoundSurvived>" + mHighiestDamageReceived1RoundSurvived + "</HighiestDamageReceived1RoundSurvived>\n";
        s += "\t\t<AdditionalGoldPerVictory>" + mAdditionalGoldPerVictory + "</AdditionalGoldPerVictory>\n";
        s += "\t\t<AdditionalGoldPerSellPercent>" + mAdditionalGoldPerSellPercent + "</AdditionalGoldPerSellPercent>\n";
        s += "\t\t<PriceCheapeningPercentWhite>" + mPriceCheapeningPercentWhite + "</PriceCheapeningPercentWhite>\n";
        s += "\t\t<PriceCheapeningPercentGreen>" + mPriceCheapeningPercentGreen + "</PriceCheapeningPercentGreen>\n";
        s += "\t\t<PriceCheapeningPercentBlack>" + mPriceCheapeningPercentBlack + "</PriceCheapeningPercentBlack>\n";
        s += "\t\t<PriceCheapeningPercentBlue>" + mPriceCheapeningPercentBlue + "</PriceCheapeningPercentBlue>\n";
        s += "\t\t<PriceCheapeningPercentRed>" + mPriceCheapeningPercentRed + "</PriceCheapeningPercentRed>\n";
        s += "\t\t<BoostersGot>" + mBoostersGot + "</BoostersGot>\n";
        s += "\t\t<MaxHPInGame>" + mMaxHPInGame + "</MaxHPInGame>\n";
        s += "\t\t<MaxDamageDealtToOtherPlayer>" + mMaxDamageDealtToOtherPlayer + "</MaxDamageDealtToOtherPlayer>\n";
        s += "\t\t<MaxDamageSurvived>" + mMaxDamageSurvived + "</MaxDamageSurvived>\n";
        s += "\t\t<CreatureMax>" + mCreatureMax + "</CreatureMax>\n";
        s += "\t\t<AvatarImage>" + UtilityString.toXML(mAvatarImage) + "</AvatarImage>\n";
        s += "\t</PlayerData>\n";
        return s;
    }

    @Override
    public String toString() {
        return mName;
    }

    private static PlayerDataXMLHandler XMLHANDLER = new PlayerDataXMLHandler();

    public static PlayerDataXMLHandler getXMLParseHandler() {
        return XMLHANDLER;
    }

    public static boolean saveCollectionAsXML(String filename, Collection<PlayerData> col) {
        try {
            PrintWriter pw = new PrintWriter(csa.Global.mBaseDir + filename, "UTF-8");
            pw.print("<?xml version=\"1.0\"?>\n");
            pw.print("<AllPlayerData>\n");
            Iterator<PlayerData> iter = col.iterator();
            while (iter.hasNext()) {
                PlayerData item = iter.next();
                pw.print(item.exportXML());
            }
            pw.print("</AllPlayerData>\n");
            pw.close();
        } catch (IOException e) {
            System.err.println(e.toString());
            return false;
        }
        return true;
    }

    public static HashMap<String, PlayerData> getHashMapFromXML(String filename) {
        HashMap<String, PlayerData> filters = new HashMap<String, PlayerData>();
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            PlayerDataXMLHandler h = PlayerData.getXMLParseHandler();
            saxParser.parse(csa.Global.mBaseDir + filename, h);
            filters = h.getLastHashMap();
        } catch (Throwable e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(null, e.toString(), "PlayerData Load Error...", JOptionPane.INFORMATION_MESSAGE);
        }
        return filters;
    }
}
