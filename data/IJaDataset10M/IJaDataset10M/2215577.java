package org.cleartk.util.collection;

import java.io.File;
import java.io.IOException;
import java.io.Writer;

/**
 * <br>
 * Copyright (c) 2007-2009, Regents of the University of Colorado <br>
 * All rights reserved.
 */
public interface Writable {

    public void write(File file) throws IOException;

    public void write(Writer writer) throws IOException;
}
