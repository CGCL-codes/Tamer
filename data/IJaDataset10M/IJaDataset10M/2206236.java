package es.eucm.eadventure.common.loader.subparsers;

import org.xml.sax.Attributes;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.NextScene;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.AbstractEffect;
import es.eucm.eadventure.common.data.chapter.effects.Effects;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Cutscene;
import es.eucm.eadventure.common.data.chapter.scenes.Slidescene;
import es.eucm.eadventure.common.data.chapter.scenes.Videoscene;

/**
 * Class to subparse slidescenes
 */
public class CutsceneSubParser extends SubParser {

    /**
     * Constant for reading nothing
     */
    private static final int READING_NONE = 0;

    /**
     * Constant for reading resources tag
     */
    private static final int READING_RESOURCES = 1;

    /**
     * Constant for reading next-scene tag
     */
    private static final int READING_NEXT_SCENE = 2;

    /**
     * Constant for subparsing nothing
     */
    private static final int SUBPARSING_NONE = 0;

    /**
     * Constant for subparsing condition tag
     */
    private static final int SUBPARSING_CONDITION = 1;

    /**
     * Constant for subparsing effect tag
     */
    private static final int SUBPARSING_EFFECT = 2;

    /**
     * Stores the current element being parsed
     */
    private int reading = READING_NONE;

    /**
     * Stores the current element being subparsed
     */
    private int subParsing = SUBPARSING_NONE;

    /**
     * Stores the current slidescene being parsed
     */
    private Cutscene cutscene;

    /**
     * Stores the current resources being parsed
     */
    private Resources currentResources;

    /**
     * Stores the current next-scene being used
     */
    private NextScene currentNextScene;

    /**
     * Stores the current conditions being parsed
     */
    private Conditions currentConditions;

    /**
     * Stores the current effects being parsed
     */
    private Effects currentEffects;

    /**
     * The subparser for the condition or effect tags
     */
    private SubParser subParser;

    /**
     * Constructor
     * 
     * @param chapter
     *            Chapter data to store the read data
     */
    public CutsceneSubParser(Chapter chapter) {
        super(chapter);
    }

    @Override
    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) {
        if (subParsing == SUBPARSING_NONE) {
            if (qName.equals("slidescene") || qName.equals("videoscene")) {
                String slidesceneId = "";
                boolean initialScene = false;
                String idTarget = "";
                int x = Integer.MIN_VALUE, y = Integer.MIN_VALUE;
                int transitionType = 0, transitionTime = 0;
                String next = "go-back";
                boolean canSkip = true;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("id")) slidesceneId = attrs.getValue(i);
                    if (attrs.getQName(i).equals("start")) initialScene = attrs.getValue(i).equals("yes");
                    if (attrs.getQName(i).equals("idTarget")) idTarget = attrs.getValue(i);
                    if (attrs.getQName(i).equals("destinyX")) x = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("destinyY")) y = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("transitionType")) transitionType = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("transitionTime")) transitionTime = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("next")) next = attrs.getValue(i);
                    if (attrs.getQName(i).equals("canSkip")) canSkip = attrs.getValue(i).equals("yes");
                }
                if (qName.equals("slidescene")) cutscene = new Slidescene(slidesceneId); else cutscene = new Videoscene(slidesceneId);
                if (initialScene) chapter.setTargetId(slidesceneId);
                cutscene.setTargetId(idTarget);
                cutscene.setPositionX(x);
                cutscene.setPositionY(y);
                cutscene.setTransitionType(transitionType);
                cutscene.setTransitionTime(transitionTime);
                if (cutscene instanceof Videoscene) ((Videoscene) cutscene).setCanSkip(canSkip);
                if (next.equals("go-back")) {
                    cutscene.setNext(Cutscene.GOBACK);
                } else if (next.equals("new-scene")) {
                    cutscene.setNext(Cutscene.NEWSCENE);
                } else if (next.equals("end-chapter")) {
                    cutscene.setNext(Cutscene.ENDCHAPTER);
                }
            } else if (qName.equals("resources")) {
                currentResources = new Resources();
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("name")) currentResources.setName(attrs.getValue(i));
                }
                reading = READING_RESOURCES;
            } else if (qName.equals("asset")) {
                String type = "";
                String path = "";
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("type")) type = attrs.getValue(i);
                    if (attrs.getQName(i).equals("uri")) path = attrs.getValue(i);
                }
                currentResources.addAsset(type, path);
            } else if (qName.equals("end-game")) {
                cutscene.setNext(Cutscene.ENDCHAPTER);
            } else if (qName.equals("next-scene")) {
                String idTarget = "";
                int x = Integer.MIN_VALUE, y = Integer.MIN_VALUE;
                int transitionType = 0, transitionTime = 0;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("idTarget")) idTarget = attrs.getValue(i);
                    if (attrs.getQName(i).equals("x")) x = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("y")) y = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("transitionType")) transitionType = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("transitionTime")) transitionTime = Integer.parseInt(attrs.getValue(i));
                }
                currentNextScene = new NextScene(idTarget, x, y);
                currentNextScene.setTransitionType(transitionType);
                currentNextScene.setTransitionTime(transitionTime);
                reading = READING_NEXT_SCENE;
            } else if (qName.equals("condition")) {
                currentConditions = new Conditions();
                subParser = new ConditionSubParser(currentConditions, chapter);
                subParsing = SUBPARSING_CONDITION;
            } else if (qName.equals("effect")) {
                currentEffects = new Effects();
                subParser = new EffectSubParser(currentEffects, chapter);
                subParsing = SUBPARSING_EFFECT;
            } else if (qName.equals("post-effect")) {
                currentEffects = new Effects();
                subParser = new EffectSubParser(currentEffects, chapter);
                subParsing = SUBPARSING_EFFECT;
            }
        }
        if (subParsing != SUBPARSING_NONE) {
            subParser.startElement(namespaceURI, sName, qName, attrs);
        }
    }

    @Override
    public void endElement(String namespaceURI, String sName, String qName) {
        if (subParsing == SUBPARSING_NONE) {
            if (qName.equals("slidescene") || qName.equals("videoscene")) {
                chapter.addCutscene(cutscene);
            } else if (qName.equals("resources")) {
                cutscene.addResources(currentResources);
                reading = READING_NONE;
            } else if (qName.equals("name")) {
                cutscene.setName(currentString.toString().trim());
            } else if (qName.equals("documentation")) {
                cutscene.setDocumentation(currentString.toString().trim());
            } else if (qName.equals("next-scene")) {
                cutscene.addNextScene(currentNextScene);
                reading = READING_NONE;
            }
            currentString = new StringBuffer();
        } else if (subParsing == SUBPARSING_CONDITION) {
            subParser.endElement(namespaceURI, sName, qName);
            if (qName.equals("condition")) {
                if (reading == READING_RESOURCES) currentResources.setConditions(currentConditions);
                if (reading == READING_NEXT_SCENE) currentNextScene.setConditions(currentConditions);
                subParsing = SUBPARSING_NONE;
            }
        } else if (subParsing == SUBPARSING_EFFECT) {
            subParser.endElement(namespaceURI, sName, qName);
            if (qName.equals("effect")) {
                if (currentNextScene != null) currentNextScene.setEffects(currentEffects); else {
                    Effects effects = cutscene.getEffects();
                    for (AbstractEffect effect : currentEffects.getEffects()) {
                        effects.add(effect);
                    }
                }
                subParsing = SUBPARSING_NONE;
            }
            if (qName.equals("post-effect")) {
                if (currentNextScene != null) currentNextScene.setPostEffects(currentEffects); else {
                    Effects effects = cutscene.getEffects();
                    for (AbstractEffect effect : currentEffects.getEffects()) {
                        effects.add(effect);
                    }
                }
                subParsing = SUBPARSING_NONE;
            }
        }
    }

    @Override
    public void characters(char[] buf, int offset, int len) {
        if (subParsing == SUBPARSING_NONE) super.characters(buf, offset, len); else subParser.characters(buf, offset, len);
    }
}
