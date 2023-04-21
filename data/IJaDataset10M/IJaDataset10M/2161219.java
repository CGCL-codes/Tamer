package com.googlecode.openwnn.legacy;

import android.view.View;
import android.content.SharedPreferences;

/**
 * The interface of candidates view manager used by {@link OpenWnn}.
 *
 * @author Copyright (C) 2008, 2009 OMRON SOFTWARE CO., LTD.  All Rights Reserved.
 */
public interface CandidatesViewManager {

    /** Size of candidates view (normal) */
    public static final int VIEW_TYPE_NORMAL = 0;

    /** Size of candidates view (full) */
    public static final int VIEW_TYPE_FULL = 1;

    /** Size of candidates view (close/non-display) */
    public static final int VIEW_TYPE_CLOSE = 2;

    /**
     * Attribute of a word (no attribute)
     * @see com.googlecode.openwnn.legacy.WnnWord
     */
    public static final int ATTRIBUTE_NONE = 0;

    /**
     * Attribute of a word (a candidate in the history list)
     * @see com.googlecode.openwnn.legacy.WnnWord
     */
    public static final int ATTRIBUTE_HISTORY = 1;

    /**
     * Attribute of a word (the best candidate)
     * @see com.googlecode.openwnn.legacy.WnnWord
     */
    public static final int ATTRIBUTE_BEST = 2;

    /**
     * Attribute of a word (auto generated/not in the dictionary)
     * @see com.googlecode.openwnn.legacy.WnnWord
     */
    public static final int ATTRIBUTE_AUTO_GENERATED = 4;

    /**
     * Initialize the candidates view.
     *
     * @param parent    The OpenWnn object
     * @param width     The width of the display
     * @param height    The height of the display
     *
     * @return The candidates view created in the initialize process; {@code null} if cannot create a candidates view.
     */
    public View initView(OpenWnn parent, int width, int height);

    /**
     * Get the candidates view being used currently.
     *
     * @return The candidates view; {@code null} if no candidates view is used currently.
     */
    public View getCurrentView();

    /**
     * Set the candidates view type.
     *
     * @param type  The candidate view type
     */
    public void setViewType(int type);

    /**
     * Get the candidates view type.
     *
     * @return      The view type
     */
    public int getViewType();

    /**
     * Display candidates.
     *
     * @param converter  The {@link WnnEngine} from which {@link CandidatesViewManager} gets the candidates
     *
     * @see com.googlecode.openwnn.legacy.WnnEngine#getNextCandidate
     */
    public void displayCandidates(WnnEngine converter);

    /**
     * Clear and hide the candidates view.
     */
    public void clearCandidates();

    /**
     * Reflect the preferences in the candidates view.
     *
     * @param pref    The preferences
     */
    public void setPreferences(SharedPreferences pref);
}
