package com.loribel.tools.sa.abstraction;

import java.io.IOException;

/**
 * Write a String Action.
 *
 * @author Gr�gory Borelli
 */
interface GB_SAFileReader {

    GB_StringActionFile read(String a_id) throws IOException;
}
