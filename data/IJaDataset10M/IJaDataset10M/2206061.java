package kdaf.loc.acquire.ibutton;

import kdaf.loc.data.ibutton.*;

public class SimplePrinter implements IRecordListener {

    private IButtonReader _reader;

    public void registerRecord(IButtonRecord rec) {
        System.out.println(rec.toString());
    }

    public void go() {
        _reader = new IButtonReader();
        _reader.registerInterest(this);
        _reader.startReader();
    }

    public static void main(String[] args) {
        SimplePrinter printer = new SimplePrinter();
        printer.go();
    }

    public void logMessage(String msg) {
        System.out.println(msg);
    }

    public void logVisit(VisitorInformation info) {
    }

    public void buttonArrived(String uid, String name) {
    }

    public void buttonLeft() {
    }
}
