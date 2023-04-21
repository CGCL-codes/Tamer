package com.agimatec.utility.fileimport;

import java.io.Writer;

/**
 * Description: <br/>
 * User: roman.stumm <br/>
 * Date: 22.11.2007 <br/>
 * Time: 11:46:35 <br/>
 * Copyright: Agimatec GmbH
 */
public interface ImporterSpec {

    /**
     * convience method - 
     * @return null or a writer for data that cannot be imported 
     */
    Writer getErrorWriter();

    /**
     * factory method - create a processor capable to process the data to be imported.
     * @param importer
     * @return
     */
    ImporterProcessor createProcessor(Importer importer);
}
