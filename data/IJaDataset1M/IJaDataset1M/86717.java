package jumbo.euclid;

/** thrown by inversion, eigenvalue routines, etc 
@author (C) P. Murray-Rust, 1996
*/
public class SingMatrixException extends IllCondMatrixException {

    public SingMatrixException() {
        super();
    }

    public SingMatrixException(String s) {
        super(s);
    }
}
