package csa.jportal.test;

import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import java.util.*;

public class TestDataXMLHandler extends DefaultHandler {

    private HashMap<String, TestData> mTestData;

    private TestData mCurrentData = null;

    private String mCurrentElement = null;

    private String mPlayer1Deck = "";

    private String mPlayer2Deck = "";

    private String mLifePlayer1 = "";

    private String mLifePlayer2 = "";

    private String mLibrary1 = "";

    private Vector<String> mLibrary1s = null;

    private String mGraveyeard1 = "";

    private Vector<String> mGraveyeard1s = null;

    private String mHand1 = "";

    private Vector<String> mHand1s = null;

    private String mLand1 = "";

    private Vector<Integer> mLand1s = null;

    private String mCreature1 = "";

    private Vector<String> mCreature1s = null;

    private String mMana1 = "";

    private Vector<Integer> mMana1s = null;

    private String mLibrary2 = "";

    private Vector<String> mLibrary2s = null;

    private String mGraveyeard2 = "";

    private Vector<String> mGraveyeard2s = null;

    private String mHand2 = "";

    private Vector<String> mHand2s = null;

    private String mLand2 = "";

    private Vector<String> mLand2s = null;

    private String mCreature2 = "";

    private Vector<String> mCreature2s = null;

    private String mMana2 = "";

    private Vector<Integer> mMana2s = null;

    public HashMap<String, TestData> getLastHashMap() {
        return mTestData;
    }

    @Override
    public void startDocument() throws SAXException {
        mCurrentData = new TestData();
        mTestData = new HashMap<String, TestData>();
    }

    @Override
    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        mCurrentElement = qName;
        if (qName.equalsIgnoreCase("TestData")) {
            mCurrentData = new TestData();
            mPlayer1Deck = "";
            mPlayer2Deck = "";
            mLifePlayer1 = "";
            mLifePlayer2 = "";
            mLibrary1 = "";
            mLibrary1s = new Vector<String>();
            mGraveyeard1 = "";
            mGraveyeard1s = new Vector<String>();
            mHand1 = "";
            mHand1s = new Vector<String>();
            mLand1 = "";
            mLand1s = new Vector<Integer>();
            mCreature1 = "";
            mCreature1s = new Vector<String>();
            mMana1 = "";
            mMana1s = new Vector<Integer>();
            mLibrary2 = "";
            mLibrary2s = new Vector<String>();
            mGraveyeard2 = "";
            mGraveyeard2s = new Vector<String>();
            mHand2 = "";
            mHand2s = new Vector<String>();
            mLand2 = "";
            mLand2s = new Vector<String>();
            mCreature2 = "";
            mCreature2s = new Vector<String>();
            mMana2 = "";
            mMana2s = new Vector<Integer>();
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) {
        String s = new String(ch, start, length);
        if (mCurrentElement == null) return;
        if (mCurrentElement.equalsIgnoreCase("Class")) mCurrentData.mClass += s;
        if (mCurrentElement.equalsIgnoreCase("Name")) mCurrentData.mName += s;
        if (mCurrentElement.equalsIgnoreCase("PLAYER1DECK")) mPlayer1Deck += s;
        if (mCurrentElement.equalsIgnoreCase("PLAYER2DECK")) mPlayer2Deck += s;
        if (mCurrentElement.equalsIgnoreCase("LIFEPLAYER1")) mLifePlayer1 += s;
        if (mCurrentElement.equalsIgnoreCase("LIFEPLAYER2")) mLifePlayer2 += s;
        if (mCurrentElement.equalsIgnoreCase("LIBRARY1")) mLibrary1 += s;
        if (mCurrentElement.equalsIgnoreCase("GRAVEYARD1")) mGraveyeard1 += s;
        if (mCurrentElement.equalsIgnoreCase("HAND1")) mHand1 += s;
        if (mCurrentElement.equalsIgnoreCase("LAND1")) mLand1 += s;
        if (mCurrentElement.equalsIgnoreCase("CREATURE1")) mCreature1 += s;
        if (mCurrentElement.equalsIgnoreCase("MANA1")) mMana1 += s;
        if (mCurrentElement.equalsIgnoreCase("LIBRARY2")) mLibrary2 += s;
        if (mCurrentElement.equalsIgnoreCase("GRAVEYARD2")) mGraveyeard2 += s;
        if (mCurrentElement.equalsIgnoreCase("HAND2")) mHand2 += s;
        if (mCurrentElement.equalsIgnoreCase("LAND2")) mLand2 += s;
        if (mCurrentElement.equalsIgnoreCase("CREATURE2")) mCreature2 += s;
        if (mCurrentElement.equalsIgnoreCase("MANA2")) mMana2 += s;
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if ("LIBRARY1".equalsIgnoreCase(qName)) {
            mLibrary1s.addElement(mLibrary1);
            mLibrary1 = "";
        }
        if ("GRAVEYARD1".equalsIgnoreCase(qName)) {
            mGraveyeard1s.addElement(mGraveyeard1);
            mGraveyeard1 = "";
        }
        if ("HAND1".equalsIgnoreCase(qName)) {
            mHand1s.addElement(mHand1);
            mHand1 = "";
        }
        if ("LAND1".equalsIgnoreCase(qName)) {
            try {
                mLand1s.addElement(Integer.parseInt(mLand1));
            } catch (Throwable e) {
            }
            mLand1 = "";
        }
        if ("CREATURE1".equalsIgnoreCase(qName)) {
            mCreature1s.addElement(mCreature1);
            mCreature1 = "";
        }
        if ("MANA1".equalsIgnoreCase(qName)) {
            try {
                mMana1s.addElement(Integer.parseInt(mMana1));
            } catch (Throwable e) {
            }
            mMana1 = "";
        }
        if ("LIBRARY2".equalsIgnoreCase(qName)) {
            mLibrary2s.addElement(mLibrary2);
            mLibrary2 = "";
        }
        if ("GRAVEYARD2".equalsIgnoreCase(qName)) {
            mGraveyeard2s.addElement(mGraveyeard2);
            mGraveyeard2 = "";
        }
        if ("HAND2".equalsIgnoreCase(qName)) {
            mHand2s.addElement(mHand2);
            mHand2 = "";
        }
        if ("LAND2".equalsIgnoreCase(qName)) {
            mLand2s.addElement(mLand2);
            mLand2 = "";
        }
        if ("CREATURE2".equalsIgnoreCase(qName)) {
            mCreature2s.addElement(mCreature2);
            mCreature2 = "";
        }
        if ("MANA2".equalsIgnoreCase(qName)) {
            try {
                mMana2s.addElement(Integer.parseInt(mMana2));
            } catch (Throwable e) {
            }
            mMana2 = "";
        }
        if ("TestData".equalsIgnoreCase(qName)) {
            if (mCurrentData != null) {
                mCurrentData.mPlayer1Deck = mPlayer1Deck;
                mPlayer1Deck = "";
                mCurrentData.mPlayer2Deck = mPlayer2Deck;
                mPlayer2Deck = "";
                try {
                    mCurrentData.mLifePlayer1 = Integer.parseInt(mLifePlayer1);
                } catch (Throwable e) {
                }
                mLifePlayer1 = "";
                try {
                    mCurrentData.mLifePlayer2 = Integer.parseInt(mLifePlayer2);
                } catch (Throwable e) {
                }
                mLifePlayer2 = "";
                mLibrary1 = "";
                mCurrentData.mLibrary1 = mLibrary1s;
                mGraveyeard1 = "";
                mCurrentData.mGraveyeard1 = mGraveyeard1s;
                mHand1 = "";
                mCurrentData.mHand1 = mHand1s;
                mLand1 = "";
                mCurrentData.mLand1 = mLand1s;
                mCreature1 = "";
                mCurrentData.mCreature1 = mCreature1s;
                mMana1 = "";
                mCurrentData.mMana1 = mMana1s;
                mLibrary2 = "";
                mCurrentData.mLibrary2 = mLibrary2s;
                mGraveyeard2 = "";
                mCurrentData.mGraveyeard2 = mGraveyeard2s;
                mHand2 = "";
                mCurrentData.mHand2 = mHand2s;
                mLand2 = "";
                mCurrentData.mLand2 = mLand2s;
                mCreature2 = "";
                mCurrentData.mCreature2 = mCreature2s;
                mMana2 = "";
                mCurrentData.mMana2 = mMana2s;
                mTestData.put(mCurrentData.mName, mCurrentData);
                mCurrentData = null;
            }
        }
        mCurrentElement = null;
    }
}
