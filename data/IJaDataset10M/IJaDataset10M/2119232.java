package com.ohrasys.cad.gds.validator;

import com.ohrasys.cad.bnf.*;
import com.ohrasys.cad.gds.*;

/**
 * A Bachus Naur test for the GDSII &lt;textbody&gt; element.
 *
 * @author   $Author: tvaline $
 * @version  $Revision: 1.11 $
 * @since    1.5
 */
public class TextbodyValidator extends BNFRequiredTest {

    /**
   * Creates a new TextbodyValidator object.
   *
   * @throws  BNFTestException  If an error occurs during list creation
   */
    public TextbodyValidator() throws BNFTestException {
        super(buildTests());
    }

    /**
   * Returns a string representation of this validator
   *
   * @return  The physical address of this object
   */
    public String toString() {
        return super.toString();
    }

    /**
   * The list of child tests that comprise the TextBodyValidator
   *
   * @return  A list of tests that comprise the Textbody element
   *
   * @throws  BNFTestException  If an error occurs during list creation
   */
    private static BNFTestImplementor[] buildTests() throws BNFTestException {
        return new BNFTestImplementor[] { new BNFNoFallthruTest(GDSRecord.TEXTTYPE), new BNFOptionalTest(new BNFTestImplementor[] { new BNFNoFallthruTest(GDSRecord.PRESENTATION) }), new BNFOptionalTest(new BNFTestImplementor[] { new BNFNoFallthruTest(GDSRecord.PATHTYPE) }), new BNFOptionalTest(new BNFTestImplementor[] { new BNFNoFallthruTest(GDSRecord.WIDTH) }), new BNFOptionalTest(new BNFTestImplementor[] { new StransValidator() }), new BNFNoFallthruTest(GDSRecord.XY), new BNFNoFallthruTest(GDSRecord.STRING) };
    }
}
