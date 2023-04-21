package com.melloware.jukes.gui.view.validation;

import com.jgoodies.validation.ValidationResult;
import com.jgoodies.validation.Validator;
import com.jgoodies.validation.util.PropertyValidationSupport;
import com.jgoodies.validation.util.ValidationUtils;
import com.melloware.jukes.db.orm.Track;
import com.melloware.jukes.gui.tool.Resources;

/**
 * Validates Tracks.
 * <p>
 * Copyright (c) 1999-2007 Melloware, Inc. <http://www.melloware.com>
 * @author Emil A. Lefkof III <info@melloware.com>
 * @version 4.0
 */
public final class TrackValidator implements Validator {

    private static final String TITLE = "Title";

    private static final String COMMENT = "Comment";

    private static final String TRACK = "Track Number";

    private static final String MESSAGE_MANDATORY = Resources.getString("messages.isMandatory");

    /**
    * Holds the track to be validated.
    */
    private final Track track;

    /**
    * Constructs a TrackValidator on the given Track.
    * @param track the track to be validated
    */
    public TrackValidator(Track track) {
        this.track = track;
    }

    /**
    * Validates this Validator's Order and returns the result as an instance of
    * {@link ValidationResult}.
    * @return the ValidationResult of the track validation
    */
    @Override
    public ValidationResult validate(Object validationTarget) {
        final PropertyValidationSupport support = new PropertyValidationSupport(track, "Track");
        if (ValidationUtils.isBlank(track.getName())) {
            support.addError(TITLE, MESSAGE_MANDATORY);
        }
        if (!ValidationUtils.hasMaximumLength(track.getName(), 100)) {
            support.addError(TITLE, Resources.getString("messages.Length100"));
        }
        if (!ValidationUtils.hasMaximumLength(track.getComment(), 254)) {
            support.addError(COMMENT, Resources.getString("messages.Length254"));
        }
        if (ValidationUtils.isBlank(track.getTrackNumber())) {
            support.addError(TRACK, MESSAGE_MANDATORY);
        } else if (!ValidationUtils.hasMinimumLength(track.getTrackNumber(), 2)) {
            support.addError(TRACK, Resources.getString("messages.Length2"));
        } else if (!ValidationUtils.isNumeric(track.getTrackNumber())) {
            support.addError(TRACK, Resources.getString("messages.mustbeanumber"));
        }
        return support.getResult();
    }
}
