package org.apache.exi.grammars.grammarRules;

import org.apache.exi.core.EXIConstants;
import org.apache.exi.core.headerOptions.HeaderPreserveRules;
import org.apache.exi.grammars.events.*;

/**
 * Schemaless DocContent grammar
 *
 * @author Sheldon L. Snyder<br>
 * @version 1.0.0<br>
 *
 *
 * Document:
 *      SD DocContent 0
 * DocContent:
 *      SE (*) DocEnd 0
 *      DT DocContent 1.0
 *      CM DocContent 1.1.0
 *      PI DocContent 1.1.1
 * DocEnd:
 *      ED 0
 *      CM DocEnd 1.0
 *      PI DocEnd 1.1
 * 
 * All productions in the built-in Document grammars of the form
 *      LeftHandSide : SE (*) RightHandSide are evaluated as follows:
 *  1. Let qname be the qname of the element matched by SE (*)
 *  2. If a grammar does not exist for element qname, create one based on the Built-in Element Grammar
 *  3. Evaluate the element contents using a built-in grammar for element qname
 *  4. Evaluate the remainder of event sequence using RightHandSide.
 * 
 */
public class GrammarRuleSchemaLessDocContent extends GrammarRuleSchemaLessAbstract {

    String start = "DocContent";

    String end = "DocEnd";

    String gramType = start;

    boolean seFirst = true;

    /**are any options set...first SE = -1 without, and 0 with...special case*/
    private boolean hasStartOptions = false;

    /** are their any options set that effect ED */
    private boolean hasEndOptions = false;

    public String getGramType() {
        return gramType;
    }

    public GrammarRuleSchemaLessDocContent() {
        super("DocContent");
        if (HeaderPreserveRules.PRESERVE_CM.isPreserved() || HeaderPreserveRules.PRESERVE_PI.isPreserved()) {
            this.setHasStartOptions(true);
            this.setHasEndOptions(true);
        }
        if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) setHasStartOptions(true);
    }

    /**
     * Determins if transiton has occured
     *
     * @param addE - event to check against
     */
    public void transition(EventAbstract addE) {
        EventType type = addE.getEventType();
        if (type.equals(EventType.SE)) {
            setTransitioned(!isTransitioned());
            if (isTransitioned()) gramType = end; else gramType = start;
        }
    }

    /**
     *  Document:
     *      SD DocContent 0         0
     *  DocContent:
     *      SE (*) DocEnd 0         n
     *      DT DocContent 1.0       n+1.0
     *      CM DocContent 1.1.0     n+1.1.0
     *      PI DocContent 1.1.1     n+1.1.1
     * ******************************************************
     * DocEnd:
     *      ED 0                    0
     *      CM DocEnd 1.0           n+1.0
     *      PI DocEnd 1.1           n+1.1
     *
     * @param addE - event to set part codes
     */
    public void setEventCode(EventAbstract addE) {
        EventType type = addE.getEventType();
        if (type.equals(EventType.SD)) {
            addE.setEventCodePart1(0);
            addE.setNumberBitsPart1(0);
        } else if (containsEvent(addE)) {
            addE.setEventCodePart1(eventListOrderedNotTransitioned.indexOf(addE.getName()));
            int result = eventListOrderedNotTransitioned.size();
            if (this.isHasStartOptions()) addE.setNumberBitsPart1(EXIConstants.howManyBits(result + 1)); else addE.setNumberBitsPart1(EXIConstants.howManyBits(result));
            addE.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
            addE.setNumberBitsPart2(0);
            addE.setEventCodePart3(EXIConstants.VALUE_NOT_YET_SET);
            addE.setNumberBitsPart3(0);
        } else {
            setCurrentEventCodePart1(addE);
            setCurrentEventCodePart2(addE);
            setCurrentEventCodePart3(addE);
        }
    }

    /**
     * Event code part 1 rules
     *
     * @param evt - event to set
     * @return results for part 1
     */
    public void setCurrentEventCodePart1(EventAbstract evt) {
        int result = getSize();
        EventType type = evt.getEventType();
        if (isTransitioned()) {
            if (this.isHasEndOptions()) evt.setNumberBitsPart1(1); else evt.setNumberBitsPart1(0);
            if (type.equals(EventType.ED)) {
                evt.setEventCodePart1(0);
            } else if (type.equals(EventType.PI) || type.equals(EventType.CM)) {
                evt.setEventCodePart1(1);
            }
        } else {
            if (this.isHasStartOptions()) {
                evt.setNumberBitsPart1(EXIConstants.howManyBits(result + 2));
            } else {
                if (result == 0) evt.setNumberBitsPart1(EXIConstants.howManyBits(0)); else evt.setNumberBitsPart1(EXIConstants.howManyBits(result + 1));
            }
            if (type.equals(EventType.SE)) {
                evt.setEventCodePart1(result);
            } else if (type.equals(EventType.CM) || type.equals(EventType.DT) || type.equals(EventType.ER) || type.equals(EventType.PI)) {
                evt.setEventCodePart1(result + 1);
            }
        }
    }

    /**
     * Event code part 2 rules
     *
     * @param evt - event to set
     * @return results for part 2
     */
    public void setCurrentEventCodePart2(EventAbstract evt) {
        int result = EXIConstants.VALUE_NOT_YET_SET;
        EventType type = evt.getEventType();
        int count = 0;
        if (isTransitioned()) {
            count = 0;
            if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
            if (type.equals(EventType.ED)) {
                evt.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
                evt.setNumberBitsPart2(0);
            } else if (type.equals(EventType.CM) && count == 2) {
                evt.setEventCodePart2(0);
                evt.setNumberBitsPart2(1);
            } else if (type.equals(EventType.CM)) {
                evt.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
                evt.setNumberBitsPart2(0);
            } else if (type.equals(EventType.PI) && count == 2) {
                evt.setEventCodePart2(1);
                evt.setNumberBitsPart2(1);
            } else if (type.equals(EventType.PI)) {
                evt.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
                evt.setNumberBitsPart2(0);
            }
        } else {
            if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) count++;
            if (type.equals(EventType.SE)) {
                evt.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
                evt.setNumberBitsPart2(0);
            } else if (type.equals(EventType.DT) && count == 1) {
                evt.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
                evt.setNumberBitsPart2(0);
            } else if (type.equals(EventType.DT) && count >= 2) {
                result = 0;
                evt.setNumberBitsPart2(1);
            } else if (type.equals(EventType.CM) && count == 1) {
                evt.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
                evt.setNumberBitsPart2(0);
            } else if (type.equals(EventType.CM) && count >= 2) {
                if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) {
                    evt.setEventCodePart2(1);
                    evt.setNumberBitsPart2(1);
                } else if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) {
                    evt.setEventCodePart2(0);
                    evt.setNumberBitsPart2(1);
                }
            } else if (type.equals(EventType.PI) && count == 1) {
                evt.setEventCodePart2(EXIConstants.VALUE_NOT_YET_SET);
                evt.setNumberBitsPart2(0);
            } else if (type.equals(EventType.PI) && count >= 2) {
                evt.setEventCodePart2(1);
                evt.setNumberBitsPart2(1);
            }
        }
    }

    /**
     * Event code part 3 rules
     *
     * @param evt - event to set
     * @return results for part 3
     */
    public void setCurrentEventCodePart3(EventAbstract evt) {
        EventType type = evt.getEventType();
        evt.setEventCodePart3(EXIConstants.VALUE_NOT_YET_SET);
        evt.setNumberBitsPart3(0);
        if (!isTransitioned()) {
            int count = 0;
            if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) count++;
            if (type.equals(EventType.CM) && count == 3) {
                evt.setEventCodePart3(0);
                evt.setNumberBitsPart3(0);
            } else if (type.equals(EventType.PI) && count == 3) {
                evt.setEventCodePart3(1);
                evt.setNumberBitsPart3(0);
            }
        }
    }

    /**
     * @return the hasOptions
     */
    public boolean isHasStartOptions() {
        return hasStartOptions;
    }

    /**
     * @param hasOptions the hasOptions to set
     */
    public void setHasStartOptions(boolean hasOptions) {
        hasStartOptions = hasOptions;
    }

    /**
     * @return the hasEndOptions
     */
    public boolean isHasEndOptions() {
        return hasEndOptions;
    }

    /**
     * @param hasEndOptions the hasEndOptions to set
     */
    public void setHasEndOptions(boolean hasEndOptions) {
        this.hasEndOptions = hasEndOptions;
    }

    /**
     * max int for event code part 1 during exi read in
     *
     * @return
     */
    public int getMaxEventCodePart1() {
        int result = EXIConstants.VALUE_NOT_YET_SET;
        if (isTransitioned()) {
            if (isHasEndOptions()) result = 1; else result = 0;
        } else {
            result = getSize();
            if (this.isHasStartOptions()) {
                result += 1;
            }
        }
        return result;
    }

    /**
     * max int for event code part 2 during exi read in
     *
     * @return
     */
    public int getMaxEventCodePart2() {
        int result = EXIConstants.VALUE_NOT_YET_SET;
        int count = 0;
        if (isTransitioned()) {
            if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
            if (count == 2) result = 1; else result = 0;
        } else {
            if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) count++;
            if (count <= 1) result = 0; else result = 1;
        }
        return result;
    }

    public int getMaxEventCodePart3() {
        int result = EXIConstants.VALUE_NOT_YET_SET;
        int count = 0;
        if (isTransitioned()) {
            result = 0;
        } else {
            if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
            if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) count++;
            if (count == 3) result = 1; else result = 0;
        }
        return result;
    }

    public EventAbstract getEventForCode(int part1, int part2, int part3) {
        int size = getSize();
        int count = 0;
        EventAbstract theEvent = null;
        if (isTransitioned()) {
            if (part1 == EXIConstants.VALUE_NOT_YET_SET || part1 == 0) theEvent = new EventEndDoc(); else {
                if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
                if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
                if ((part2 == 0 && count == 2) || (part2 == 0 && count == 1 && HeaderPreserveRules.PRESERVE_CM.isPreserved())) theEvent = new EventComment(); else theEvent = new EventProcessingInstruction();
            }
        } else {
            if (part1 < size || part1 == 0) theEvent = new EventStartElement(); else {
                if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) count++;
                if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) count++;
                if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) count++;
                if (count == 1) {
                    if (HeaderPreserveRules.PRESERVE_CM.isPreserved()) theEvent = new EventComment();
                    if (HeaderPreserveRules.PRESERVE_PI.isPreserved()) theEvent = new EventProcessingInstruction();
                    if (HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) theEvent = new EventDTD();
                }
                if (count == 2) {
                    if (part2 == 0 && HeaderPreserveRules.PRESERVE_DTD_ENTITY.isPreserved()) theEvent = new EventDTD();
                    if (part2 == 0 && HeaderPreserveRules.PRESERVE_CM.isPreserved()) theEvent = new EventComment();
                    if (part2 == 1 && HeaderPreserveRules.PRESERVE_PI.isPreserved()) theEvent = new EventProcessingInstruction(); else theEvent = new EventComment();
                }
                if (count == 3) {
                    if (part2 == 0) theEvent = new EventDTD();
                    if (part3 == 0) theEvent = new EventComment(); else theEvent = new EventProcessingInstruction();
                }
            }
        }
        return theEvent;
    }
}
