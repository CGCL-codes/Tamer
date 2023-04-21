package net.sf.magicmap.client.measurement;

import java.util.HashMap;
import net.sf.magicmap.client.measurement.interfaces.AbstractScanResult;
import net.sf.magicmap.client.measurement.interfaces.AbstractScanner;
import net.sf.magicmap.client.measurement.interfaces.AbstractScannerHandler;

public final class ScannerAllocator {

    public final HashMap<AbstractScanner, AbstractScannerHandler> handlerMap;

    boolean stopped = false;

    public ScannerAllocator() {
        this.handlerMap = new HashMap<AbstractScanner, AbstractScannerHandler>();
    }

    public void addScanResultHandler(AbstractScannerHandler handler) {
        this.handlerMap.put(handler.canHandle(), handler);
    }

    public void removeScanResultHandler(AbstractScannerHandler handler) {
        this.handlerMap.remove(handler.canHandle());
    }

    public AbstractScannerHandler getHandler(AbstractScanner canHandleScanner) {
        if (this.stopped) return null;
        return this.handlerMap.get(canHandleScanner);
    }

    public boolean handleScanResult(AbstractScanResult scanResult, AbstractScanner handleScanner) {
        AbstractScannerHandler handler = getHandler(handleScanner);
        if (handler != null) {
            handler.scanResult(scanResult);
            return true;
        }
        return false;
    }

    public boolean handleRoundComplete(AbstractScanner scanner) {
        AbstractScannerHandler handler = getHandler(scanner);
        if (handler != null) {
            handler.roundComplete();
            return true;
        }
        return false;
    }

    public void startAllScanner() {
        this.stopped = false;
        for (AbstractScanner as : this.handlerMap.keySet()) as.start();
    }

    public void stopAllScanner() {
        this.stopped = true;
        for (AbstractScanner as : this.handlerMap.keySet()) as.stop();
    }
}
