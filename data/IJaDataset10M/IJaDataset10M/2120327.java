package org.tigr.cloe.model.facade.assemblerFacade.assemblerDataConverter;

/**
 * User: aresnick
 * Date: Mar 22, 2010
 * Time: 11:34:16 AM
 * <p/>
 * $HeadURL$
 * $LastChangedRevision$
 * $LastChangedBy$
 * $LastChangedDate$
 * <p/>
 * Description:
 */
public interface AssemblerInputFileConverter extends AssemblerConverter {

    void convertXMLRequestToAssemblyInputFiles(byte[] assemblyRequestXML) throws Exception;
}
