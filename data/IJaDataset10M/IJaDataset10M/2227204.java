package org.oclc.da.ndiipp.packager.data;

/**
  * PDIHardwareElement
  *
  * A class that represents an extended PDIElement. The
  * extended element contains an additional qualifiers (Memory and Microprocessor)
  * @author MS
  * @version 1.0
  *
  */
public class PDIHardwareElement extends PDIElement {

    private String m_strMemoryQualifier;

    private String m_strMicroprocessorQualifier;

    /** Construct an empty PDIHardwareElement object
     */
    public PDIHardwareElement() {
        m_strMemoryQualifier = null;
        m_strMicroprocessorQualifier = null;
    }

    /** Set the Memory qualifier for this PDI data element
     *  @param  strMemoryQualifier Memory qualifier       
     */
    public void setMemoryQualifier(String strMemoryQualifier) {
        m_strMemoryQualifier = strMemoryQualifier;
    }

    /** Set the Microprocessor qualifier for this PDI data element
     *  @param  strMicroprocessorQualifier Microprocessor qualifier       
     */
    public void setMicroprocessorQualifier(String strMicroprocessorQualifier) {
        m_strMicroprocessorQualifier = strMicroprocessorQualifier;
    }

    /** Returns a descriptive rendering of the PDIElement
	 * @return string
     */
    public String toString() {
        String strElementDesc;
        strElementDesc = super.toString();
        strElementDesc = strElementDesc + "," + " Memory " + m_strMemoryQualifier + "\n";
        strElementDesc = strElementDesc + "," + " Microprocessor " + m_strMicroprocessorQualifier;
        return strElementDesc;
    }

    /** Retrieve the Memory Qualifer
	 * @return Memory qualifier
	 */
    public String getMemoryQualifier() {
        return m_strMemoryQualifier;
    }

    /** Retrieve the Microprocessor Qualifer
   	* @return microprocessor qual
    */
    public String getMicroprocessorQualifier() {
        return m_strMicroprocessorQualifier;
    }
}
