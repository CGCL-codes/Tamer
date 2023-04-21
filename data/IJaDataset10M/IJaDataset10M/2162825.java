package net.sourceforge.omov.core.tools.scan;

import java.util.List;
import net.sourceforge.omov.core.BusinessException;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public interface IScanListener {

    void doDirectoryCount(int count);

    void doNextPhase(String phaseName);

    void doNextFinished();

    List<ScannedMovie> doEnhanceWithMetaData(List<ScannedMovie> scannedMovies, List<ScanHint> hints, IScannerStopped stopped, IScanListener listener) throws BusinessException;

    void doScanFinished(List<ScannedMovie> scannedMovies, List<ScanHint> hints);
}
