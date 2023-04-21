package edu.caltech.trigjclient;

import edu.caltech.sbw.*;

/**
 * @author fbergman
 *
 */
public interface Trigonometry {

    double sin(double x) throws SBWException;

    double cos(double x) throws SBWException;
}
