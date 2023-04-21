package joelib2.feature;

import joelib2.io.IOType;
import wsi.ra.text.DecimalFormatter;

/**
 * Interface definition for descriptor results which use number formats.
 *
 * @.author     wegnerj
 * @.license GPL
 * @.cvsversion    $Revision: 1.8 $, $Date: 2005/02/17 16:48:30 $
 */
public interface NumberFormatResult {

    String toString(IOType ioType, DecimalFormatter format);
}
