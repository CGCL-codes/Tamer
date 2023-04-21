package com.knitml.engine;

import java.util.List;
import com.knitml.core.common.NeedleStyle;
import com.knitml.engine.common.CannotPutMarkerOnEndOfNeedleException;
import com.knitml.engine.common.CannotWorkThroughMarkerException;
import com.knitml.engine.common.NeedlesInWrongDirectionException;
import com.knitml.engine.common.NoMarkerFoundException;
import com.knitml.engine.common.NotEnoughStitchesException;
import com.knitml.engine.settings.Direction;

/**
 * Models the interactions between the knitter and a particular knitting needle.
 * This class supports all knitting operations. Such operations include casting
 * on, knitting, purling, placing markers, slipping stitches, etc.
 * 
 * This class does not attempt to model the concept of rows, knitting style
 * (i.e. flat or in-the-round), or multiple needles. Such things are handled by
 * a client to this class, such as a
 * {@link com.knitml.engine.validation.engine.KnittingEngine}.
 * 
 * @author Jonathan Whitall (fiddlerpianist@gmail.com)
 */
public interface Needle {

    void setDirection(Direction direction);

    void knit() throws NotEnoughStitchesException;

    void purl() throws NotEnoughStitchesException;

    void slip() throws NotEnoughStitchesException;

    void reverseSlip() throws NotEnoughStitchesException;

    void placeMarker(Marker marker) throws CannotPutMarkerOnEndOfNeedleException;

    Marker removeMarker() throws NoMarkerFoundException;

    Stitch removeNextStitch() throws NotEnoughStitchesException;

    int getStitchesToGap();

    void turn();

    void knitTwoTogether() throws NotEnoughStitchesException, CannotWorkThroughMarkerException;

    void increase(int numberOfStitches);

    void workIntoNextStitch(int numberToWork) throws NotEnoughStitchesException;

    /**
	 * @return the total number of stitches currently on this needle
	 */
    int getTotalStitches();

    /**
	 * @return the number of stitches remaining to work on this needle
	 */
    int getStitchesRemaining();

    boolean areMarkersRemaining();

    boolean isEndOfNeedle();

    boolean isBeginningOfNeedle();

    void startAtBeginning();

    void startAtEnd();

    /**
	 * The next stitch is defined as the stitch which is the next stitch to be
	 * worked.
	 * 
	 * @return
	 */
    int getNextStitchIndex();

    /**
	 * The previous stitch is defined as the stitch which is the stitch that was
	 * just worked.
	 * 
	 * @return
	 */
    int getPreviousStitchIndex();

    Stitch peekAtNextStitch();

    int getStitchesToNextMarker() throws NoMarkerFoundException;

    void purlTwoTogether() throws NotEnoughStitchesException, CannotWorkThroughMarkerException;

    void addStitchesToBeginning(List<Stitch> stitchesToAdd) throws NeedlesInWrongDirectionException;

    void addStitchesToEnd(List<Stitch> stitchesToAdd) throws NeedlesInWrongDirectionException;

    /**
	 * Returns the current view of stitches on the needle. The view is adjusted
	 * for either forwards or backwards needle direction, so that the sequence
	 * of stitches returned by this method will always be from left to right.
	 * 
	 * @return
	 */
    List<Stitch> getStitches();

    /**
	 * @param number
	 * @return
	 * @throws NeedlesInWrongDirectionException
	 */
    List<Stitch> removeNStitchesFromBeginning(int number) throws NeedlesInWrongDirectionException;

    /**
	 * @param number
	 * @return
	 * @throws NeedlesInWrongDirectionException
	 */
    List<Stitch> removeNStitchesFromEnd(int number) throws NeedlesInWrongDirectionException;

    boolean hasGaps();

    Direction getDirection();

    NeedleStyle getNeedleType();

    void setNeedleType(NeedleStyle needleType);

    String getId();

    void passPreviousStitchOver() throws NotEnoughStitchesException;

    void knitThreeTogether() throws CannotWorkThroughMarkerException, NotEnoughStitchesException;

    void cross(int first, int next) throws CannotWorkThroughMarkerException, NotEnoughStitchesException;
}
