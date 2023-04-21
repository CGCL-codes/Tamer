package com.knitml.validation.visitor.model;

import static com.knitml.core.xml.Attributes.ID;
import static com.knitml.core.xml.Attributes.UNTIL;
import static com.knitml.core.xml.Attributes.VALUE;
import static com.knitml.core.common.EnumUtils.toEnum;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Attribute;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.knitml.core.common.ValidationException;
import com.knitml.core.model.Repeat.Until;
import com.knitml.validation.common.KnittingEngineException;
import com.knitml.validation.common.NoGapFoundException;
import com.knitml.validation.common.NoMarkerFoundException;
import com.knitml.validation.context.KnittingContext;
import com.knitml.validation.recorder.Recording;
import com.knitml.validation.visitor.impl.AbstractValidationVisitor;

public class RepeatVisitor extends AbstractValidationVisitor {

    @SuppressWarnings("unused")
    private static final Logger log = LoggerFactory.getLogger(RepeatVisitor.class);

    public void visit(Element element, KnittingContext context) throws KnittingEngineException {
        Attribute id = element.attribute(ID);
        if (id == null) {
            String generatedId = context.getIdGenerator().generateNewId();
            element.addAttribute(ID, generatedId);
            id = element.attribute(ID);
        }
        Recording recording = context.getRecorder().getRecording(id.getValue());
        if (recording == null) {
            recording = recordVisitChildren(id.getValue(), element, context);
        } else {
            visitChildren(element, context);
        }
        processRepeats(element, context, recording);
    }

    private void processRepeats(Element element, KnittingContext context, Recording recording) throws KnittingEngineException {
        String untilString = element.attributeValue(UNTIL);
        if (StringUtils.isBlank(untilString)) {
            throw new ValidationException("A repeat element must have an [until] attribute defined");
        }
        Until until = toEnum(untilString, Until.class);
        Attribute valueAttribute = element.attribute(VALUE);
        switch(until) {
            case TIMES:
                handleTimes(valueAttribute, context, recording);
                break;
            case BEFORE_END:
                handleBeforeEnd(valueAttribute, context, recording);
                break;
            case BEFORE_GAP:
                handleBeforeGap(valueAttribute, context, recording);
                break;
            case BEFORE_MARKER:
                handleBeforeMarker(valueAttribute, context, recording);
                break;
            case END:
                handleEnd(valueAttribute, context, recording);
                break;
            case MARKER:
                handleMarker(valueAttribute, context, recording);
                break;
            default:
        }
    }

    private void handleMarker(Attribute valueAttribute, KnittingContext context, Recording recording) throws NoMarkerFoundException {
        if (valueAttribute != null) {
            throw new ValidationException("The [value] attribute is disallowed when [until] is set to \"marker\"");
        }
        playToBeforeMarker(0, context, recording);
    }

    private void handleBeforeMarker(Attribute valueAttribute, KnittingContext context, Recording recording) throws NoMarkerFoundException, ValidationException {
        playToBeforeMarker(getValue(valueAttribute), context, recording);
    }

    private void playToBeforeMarker(int target, KnittingContext context, Recording recording) throws NoMarkerFoundException {
        while (getStitchesBeforeMarker(context) > target) {
            recording.playback();
        }
    }

    private void playToBeforeGap(int target, KnittingContext context, Recording recording) throws NoGapFoundException {
        while (getStitchesBeforeGap(context) > target) {
            recording.playback();
        }
    }

    private void handleEnd(Attribute valueAttribute, KnittingContext context, Recording recording) {
        if (valueAttribute != null) {
            throw new ValidationException("The [value] attribute is disallowed when [until] is set to \"end\"");
        }
        playToBeforeEnd(0, context, recording);
    }

    private void handleBeforeEnd(Attribute valueAttribute, KnittingContext context, Recording recording) {
        playToBeforeEnd(getValue(valueAttribute), context, recording);
    }

    private void playToBeforeEnd(int target, KnittingContext context, Recording recording) {
        while (getStitchesBeforeEnd(context) > target) {
            recording.playback();
        }
    }

    private void handleBeforeGap(Attribute valueAttribute, KnittingContext context, Recording recording) throws NoGapFoundException, ValidationException {
        playToBeforeGap(getValue(valueAttribute), context, recording);
    }

    private void handleTimes(Attribute valueAttribute, KnittingContext context, Recording recording) {
        int value = getValue(valueAttribute);
        for (int i = 0; i < value - 1; i++) {
            recording.playback();
        }
    }

    private int getValue(Attribute valueAttribute) throws ValidationException {
        if (valueAttribute == null || StringUtils.isBlank(valueAttribute.getValue())) {
            throw new ValidationException("The attribute [value] must be specified when the attribute [until] is set to \"" + "\"");
        }
        int value = Integer.parseInt(valueAttribute.getValue());
        if (value < 1) {
            throw new ValidationException("The value attribute must be a positive number");
        }
        return value;
    }

    private Recording recordVisitChildren(String id, Element element, KnittingContext context) throws KnittingEngineException {
        context.getRecorder().startRecording(id);
        visitChildren(element, context);
        context.getRecorder().stopRecording(id);
        return context.getRecorder().getRecording(id);
    }
}
