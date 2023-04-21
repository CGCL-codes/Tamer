package org.openscience.cdk.io.formats;

/**
 * @cdk.module io
 * @cdk.set    io-formats
 */
public class IChIFormat implements ChemFormatMatcher {

    public IChIFormat() {
    }

    public String getFormatName() {
        return "IUPAC Chemical Identifier";
    }

    public String getReaderClassName() {
        return "org.openscience.cdk.io.IChIReader";
    }

    public String getWriterClassName() {
        return null;
    }

    public boolean matches(int lineNumber, String line) {
        if (line.indexOf("<identifier") != -1) {
            return true;
        } else if (line.indexOf("<IChI") != -1) {
            return true;
        }
        return false;
    }
}
