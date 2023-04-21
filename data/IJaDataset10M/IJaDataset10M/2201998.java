package spidr.applets.ptolemy.plot;

/** * Exception thrown by plot classes if there are format * problems with the data to be plotted. * * @author Christopher Hylands * @version $Id: PlotDataException.java,v 1.1.1.1 2009-06-15 16:04:31 elespuru Exp $ */
class PlotDataException extends Throwable {

    public PlotDataException() {
        super();
    }

    public PlotDataException(String s) {
        super(s);
    }
}
