package jumbo.euclid;

/** interface for allowing Objects to be sorted
@author Copyright Peter Murray-Rust, 1997
*/
public interface Sortable {

    /** 
@return -1 if this < sortable; 1 if > sortable; 0 if equal
*/
    public int compareTo(Sortable sortable);
}
