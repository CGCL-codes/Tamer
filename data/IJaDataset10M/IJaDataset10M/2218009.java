package es.eucm.eadventure.common.loader.subparsers;

import java.awt.Point;
import java.util.ArrayList;
import org.xml.sax.Attributes;
import es.eucm.eadventure.common.data.chapter.Chapter;
import es.eucm.eadventure.common.data.chapter.ElementReference;
import es.eucm.eadventure.common.data.chapter.Exit;
import es.eucm.eadventure.common.data.chapter.ExitLook;
import es.eucm.eadventure.common.data.chapter.InfluenceArea;
import es.eucm.eadventure.common.data.chapter.NextScene;
import es.eucm.eadventure.common.data.chapter.conditions.Conditions;
import es.eucm.eadventure.common.data.chapter.effects.Effects;
import es.eucm.eadventure.common.data.chapter.resources.Resources;
import es.eucm.eadventure.common.data.chapter.scenes.Scene;

/**
 * Class to subparse scenes
 */
public class SceneSubParser extends SubParser {

    /**
     * Constant for reading nothing
     */
    private static final int READING_NONE = 0;

    /**
     * Constant for reading resources tag
     */
    private static final int READING_RESOURCES = 1;

    /**
     * Constant for reading exit tag
     */
    private static final int READING_EXIT = 2;

    /**
     * Constant for reading next-scene tag
     */
    private static final int READING_NEXT_SCENE = 3;

    /**
     * Constant for reading element reference tag
     */
    private static final int READING_ELEMENT_REFERENCE = 4;

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
     * Constant for subparsing active area
     */
    private static final int SUBPARSING_ACTIVE_AREA = 3;

    /**
     * Constant for subparsing active area
     */
    private static final int SUBPARSING_BARRIER = 4;

    private static final int SUBPARSING_TRAJECTORY = 5;

    /**
     * Stores the current element being parsed
     */
    private int reading = READING_NONE;

    /**
     * Stores the current element being subparsed
     */
    private int subParsing = SUBPARSING_NONE;

    /**
     * Stores the element being parsed
     */
    private Scene scene;

    /**
     * Stores the current resources being parsed
     */
    private Resources currentResources;

    /**
     * Stores the current exit being used
     */
    private Exit currentExit;

    /**
     * Stores the current exit look being used
     */
    private ExitLook currentExitLook;

    /**
     * Stores the current next-scene being used
     */
    private NextScene currentNextScene;

    private Point currentPoint;

    /**
     * Stores the current element reference being used
     */
    private ElementReference currentElementReference;

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
    public SceneSubParser(Chapter chapter) {
        super(chapter);
    }

    @Override
    public void startElement(String namespaceURI, String sName, String qName, Attributes attrs) {
        if (subParsing == SUBPARSING_NONE) {
            if (qName.equals("scene")) {
                String sceneId = "";
                boolean initialScene = false;
                int playerLayer = -1;
                float playerScale = 1.0f;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("id")) sceneId = attrs.getValue(i);
                    if (attrs.getQName(i).equals("start")) initialScene = attrs.getValue(i).equals("yes");
                    if (attrs.getQName(i).equals("playerLayer")) playerLayer = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("playerScale")) playerScale = Float.parseFloat(attrs.getValue(i));
                }
                scene = new Scene(sceneId);
                scene.setPlayerLayer(playerLayer);
                scene.setPlayerScale(playerScale);
                if (initialScene) chapter.setTargetId(sceneId);
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
            } else if (qName.equals("default-initial-position")) {
                int x = Integer.MIN_VALUE, y = Integer.MIN_VALUE;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("x")) x = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("y")) y = Integer.parseInt(attrs.getValue(i));
                }
                scene.setDefaultPosition(x, y);
            } else if (qName.equals("exit")) {
                int x = 0, y = 0, width = 0, height = 0;
                boolean rectangular = true;
                int influenceX = 0, influenceY = 0, influenceWidth = 0, influenceHeight = 0;
                boolean hasInfluence = false;
                String idTarget = "";
                int destinyX = Integer.MIN_VALUE, destinyY = Integer.MIN_VALUE;
                int transitionType = 0, transitionTime = 0;
                boolean notEffects = false;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("rectangular")) rectangular = attrs.getValue(i).equals("yes");
                    if (attrs.getQName(i).equals("x")) x = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("y")) y = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("width")) width = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("height")) height = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("hasInfluenceArea")) hasInfluence = attrs.getValue(i).equals("yes");
                    if (attrs.getQName(i).equals("influenceX")) influenceX = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("influenceY")) influenceY = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("influenceWidth")) influenceWidth = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("influenceHeight")) influenceHeight = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("idTarget")) idTarget = attrs.getValue(i);
                    if (attrs.getQName(i).equals("destinyX")) destinyX = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("destinyY")) destinyY = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("transitionType")) transitionType = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("transitionTime")) transitionTime = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("not-effects")) notEffects = attrs.getValue(i).equals("yes");
                }
                currentExit = new Exit(rectangular, x, y, width, height);
                currentExit.setNextSceneId(idTarget);
                currentExit.setDestinyX(destinyX);
                currentExit.setDestinyY(destinyY);
                currentExit.setTransitionTime(transitionTime);
                currentExit.setTransitionType(transitionType);
                currentExit.setHasNotEffects(notEffects);
                if (hasInfluence) {
                    InfluenceArea influenceArea = new InfluenceArea(influenceX, influenceY, influenceWidth, influenceHeight);
                    currentExit.setInfluenceArea(influenceArea);
                }
                reading = READING_EXIT;
            } else if (qName.equals("exit-look")) {
                currentExitLook = new ExitLook();
                String text = null;
                String cursorPath = null;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("text")) text = attrs.getValue(i);
                    if (attrs.getQName(i).equals("cursor-path")) cursorPath = attrs.getValue(i);
                }
                currentExitLook.setCursorPath(cursorPath);
                currentExitLook.setExitText(text);
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
            } else if (qName.equals("point")) {
                int x = 0;
                int y = 0;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("x")) x = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("y")) y = Integer.parseInt(attrs.getValue(i));
                }
                currentPoint = new Point(x, y);
            } else if (qName.equals("object-ref") || qName.equals("character-ref") || qName.equals("atrezzo-ref")) {
                String idTarget = "";
                int x = 0, y = 0;
                float scale = 0;
                int layer = 0;
                int influenceX = 0, influenceY = 0, influenceWidth = 0, influenceHeight = 0;
                boolean hasInfluence = false;
                for (int i = 0; i < attrs.getLength(); i++) {
                    if (attrs.getQName(i).equals("idTarget")) idTarget = attrs.getValue(i);
                    if (attrs.getQName(i).equals("x")) x = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("y")) y = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("scale")) scale = Float.parseFloat(attrs.getValue(i));
                    if (attrs.getQName(i).equals("layer")) layer = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("hasInfluenceArea")) hasInfluence = attrs.getValue(i).equals("yes");
                    if (attrs.getQName(i).equals("influenceX")) influenceX = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("influenceY")) influenceY = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("influenceWidth")) influenceWidth = Integer.parseInt(attrs.getValue(i));
                    if (attrs.getQName(i).equals("influenceHeight")) influenceHeight = Integer.parseInt(attrs.getValue(i));
                }
                if (layer == -1) layer = 0;
                currentElementReference = new ElementReference(idTarget, x, y, layer);
                if (hasInfluence) {
                    InfluenceArea influenceArea = new InfluenceArea(influenceX, influenceY, influenceWidth, influenceHeight);
                    currentElementReference.setInfluenceArea(influenceArea);
                }
                if (scale > 0.001 || scale < -0.001) currentElementReference.setScale(scale);
                reading = READING_ELEMENT_REFERENCE;
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
            } else if (qName.equals("not-effect")) {
                currentEffects = new Effects();
                subParser = new EffectSubParser(currentEffects, chapter);
                subParsing = SUBPARSING_EFFECT;
            } else if (qName.equals("active-area")) {
                subParsing = SUBPARSING_ACTIVE_AREA;
                subParser = new ActiveAreaSubParser(chapter, scene, scene.getActiveAreas().size());
            } else if (qName.equals("barrier")) {
                subParsing = SUBPARSING_BARRIER;
                subParser = new BarrierSubParser(chapter, scene, scene.getBarriers().size());
            } else if (qName.equals("trajectory")) {
                subParsing = SUBPARSING_TRAJECTORY;
                subParser = new TrajectorySubParser(chapter, scene);
            }
        }
        if (subParsing != SUBPARSING_NONE) {
            subParser.startElement(namespaceURI, sName, qName, attrs);
        }
    }

    @Override
    public void endElement(String namespaceURI, String sName, String qName) {
        if (subParsing == SUBPARSING_NONE) {
            if (qName.equals("scene")) {
                chapter.addScene(scene);
            } else if (qName.equals("resources")) {
                scene.addResources(currentResources);
                reading = READING_NONE;
            } else if (qName.equals("name")) {
                scene.setName(currentString.toString().trim());
            } else if (qName.equals("documentation")) {
                if (reading == READING_NONE) scene.setDocumentation(currentString.toString().trim()); else if (reading == READING_EXIT) currentExit.setDocumentation(currentString.toString().trim()); else if (reading == READING_ELEMENT_REFERENCE) currentElementReference.setDocumentation(currentString.toString().trim());
            } else if (qName.equals("exit")) {
                if (currentExit.getNextScenes().size() > 0) {
                    for (NextScene nextScene : currentExit.getNextScenes()) {
                        try {
                            Exit exit = (Exit) currentExit.clone();
                            exit.setNextScenes(new ArrayList<NextScene>());
                            exit.setDestinyX(nextScene.getPositionX());
                            exit.setDestinyY(nextScene.getPositionY());
                            exit.setEffects(nextScene.getEffects());
                            exit.setPostEffects(nextScene.getPostEffects());
                            if (exit.getDefaultExitLook() == null) exit.setDefaultExitLook(nextScene.getExitLook()); else {
                                if (nextScene.getExitLook() != null) {
                                    if (nextScene.getExitLook().getExitText() != null && !nextScene.getExitLook().getExitText().equals("")) exit.getDefaultExitLook().setExitText(nextScene.getExitLook().getExitText());
                                    if (nextScene.getExitLook().getCursorPath() != null && !nextScene.getExitLook().getCursorPath().equals("")) exit.getDefaultExitLook().setCursorPath(nextScene.getExitLook().getCursorPath());
                                }
                            }
                            exit.setHasNotEffects(false);
                            exit.setConditions(nextScene.getConditions());
                            exit.setNextSceneId(nextScene.getTargetId());
                            scene.addExit(exit);
                        } catch (CloneNotSupportedException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    scene.addExit(currentExit);
                }
                reading = READING_NONE;
            } else if (qName.equals("exit-look")) {
                if (reading == READING_NEXT_SCENE) currentNextScene.setExitLook(currentExitLook); else if (reading == READING_EXIT) {
                    currentExit.setDefaultExitLook(currentExitLook);
                }
            } else if (qName.equals("next-scene")) {
                currentExit.addNextScene(currentNextScene);
                reading = READING_NONE;
            } else if (qName.equals("point")) {
                currentExit.addPoint(currentPoint);
            } else if (qName.equals("object-ref")) {
                scene.addItemReference(currentElementReference);
                reading = READING_NONE;
            } else if (qName.equals("character-ref")) {
                scene.addCharacterReference(currentElementReference);
                reading = READING_NONE;
            } else if (qName.equals("atrezzo-ref")) {
                scene.addAtrezzoReference(currentElementReference);
                reading = READING_NONE;
            }
            currentString = new StringBuffer();
        } else if (subParsing == SUBPARSING_CONDITION) {
            subParser.endElement(namespaceURI, sName, qName);
            if (qName.equals("condition")) {
                if (reading == READING_RESOURCES) currentResources.setConditions(currentConditions);
                if (reading == READING_NEXT_SCENE) currentNextScene.setConditions(currentConditions);
                if (reading == READING_ELEMENT_REFERENCE) currentElementReference.setConditions(currentConditions);
                if (reading == READING_EXIT) currentExit.setConditions(currentConditions);
                subParsing = SUBPARSING_NONE;
            }
        } else if (subParsing == SUBPARSING_EFFECT) {
            subParser.endElement(namespaceURI, sName, qName);
            if (qName.equals("effect")) {
                if (reading == READING_NEXT_SCENE) currentNextScene.setEffects(currentEffects);
                if (reading == READING_EXIT) currentExit.setEffects(currentEffects);
                subParsing = SUBPARSING_NONE;
            }
            if (qName.equals("post-effect")) {
                if (reading == READING_NEXT_SCENE) currentNextScene.setPostEffects(currentEffects);
                if (reading == READING_EXIT) currentExit.setPostEffects(currentEffects);
                subParsing = SUBPARSING_NONE;
            }
            if (qName.equals("not-effect")) {
                currentExit.setNotEffects(currentEffects);
                subParsing = SUBPARSING_NONE;
            }
        } else if (subParsing == SUBPARSING_ACTIVE_AREA) {
            subParser.endElement(namespaceURI, sName, qName);
            if (qName.equals("active-area")) {
                subParsing = SUBPARSING_NONE;
            }
        } else if (subParsing == SUBPARSING_BARRIER) {
            subParser.endElement(namespaceURI, sName, qName);
            if (qName.equals("barrier")) {
                subParsing = SUBPARSING_NONE;
            }
        } else if (subParsing == SUBPARSING_TRAJECTORY) {
            subParser.endElement(namespaceURI, sName, qName);
            if (qName.equals("trajectory")) {
                subParsing = SUBPARSING_NONE;
            }
        }
    }

    @Override
    public void characters(char[] buf, int offset, int len) {
        if (subParsing == SUBPARSING_NONE) super.characters(buf, offset, len); else subParser.characters(buf, offset, len);
    }
}
