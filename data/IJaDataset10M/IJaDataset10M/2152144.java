package net.sf.saxon.style;

/**
* xsl:import element in the stylesheet. <br>
*/
public class XSLImport extends XSLGeneralIncorporate {

    /**
    * isImport() returns true if this is an xsl:import statement rather than an xsl:include
    */
    public boolean isImport() {
        return true;
    }
}
