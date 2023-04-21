package net.sf.ideoreport.api.enginestructure;

import java.io.OutputStream;
import net.sf.ideoreport.api.configuration.IConfigurable;
import net.sf.ideoreport.api.datastructure.containers.data.DataContainer;
import net.sf.ideoreport.api.datastructure.containers.parameter.IParameterValues;
import net.sf.ideoreport.api.enginestructure.exception.EngineException;

/**
 * Interface d�crivant le comportement d'un moteur
 * de rapport.
 *
 * @author roset
 */
public interface IEngine extends IConfigurable {

    /**
     * G�n�ration d�un rapport � partir de donn�es en entr�e et de param�tres.
     *
     * @param pDataContainer Data container contenant les donn�es
     * @param pOut Flux de sortie
     * @param param�tre d'entr�e du rapport.
     * @return True si ok
     * @throws EngineException en cas d'erreur lors de la g�n�ration du document
     */
    boolean process(DataContainer pDataContainer, OutputStream pOut, IParameterValues pReportParameterValues) throws EngineException;
}
