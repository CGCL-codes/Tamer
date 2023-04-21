package org.retro.scheme;

/**
 * The bot libraries and databases require default scheme functions, this class
 * contains static methods for setting those functions.
 *
 * <h2>Useful Scheme Functions(some are default scheme functions)</h2> 
 * <ul>
 *  <li>set-list-ref [ items ] [ n ] [ val ]  - change a value in a list to 'val' offset by n
 *  <li>find-firstn [ list ] [ n ] - return the first 'n' elements in a list
 *  <li>list-tail -  '(a b c d e f) 2) --> (c d e f)
 * </ul>
 *
 * @author Berlin Brown
 */
public class SchemeEvalStartup {

    private static final String _endl = "\n";

    /**
	 * 
	 * @return String 
	 */
    public static String getDefaultInit() {
        StringBuffer _sbuf = new StringBuffer();
        _sbuf.append("(define (msg-beta)" + _endl);
        _sbuf.append("    display '\"Scheme-Object loaded[2]...[ OK ]\")" + _endl);
        _sbuf.append("(define (find-element Item List)" + _endl);
        _sbuf.append("    (cond ((null? List) #f)" + _endl);
        _sbuf.append("    ((equal? Item (car List)) #t)" + _endl);
        _sbuf.append("    (else (find-element Item (cdr List)))))" + _endl);
        _sbuf.append("(define (set-list-ref items n val)" + _endl);
        _sbuf.append(" (if (= n 0)" + _endl);
        _sbuf.append("  (set-car! items val)" + _endl);
        _sbuf.append("  (set-list-ref (cdr items) (- n 1) val)))" + _endl);
        _sbuf.append("(define (list-copy lis)" + _endl);
        _sbuf.append(" (cond ((null? lis)" + _endl);
        _sbuf.append("     '())" + _endl);
        _sbuf.append("  (else" + _endl);
        _sbuf.append("     (cons (car lis)" + _endl);
        _sbuf.append("   (list-copy (cdr lis))))))" + _endl);
        _sbuf.append("(define (distinct? items)" + _endl);
        _sbuf.append("  (cond ((null? items) #t)" + _endl);
        _sbuf.append("  ((null? (cdr items)) #t)" + _endl);
        _sbuf.append("  ((member (car items) (cdr items)) #f)" + _endl);
        _sbuf.append("  (else (distinct? (cdr items)))))" + _endl);
        _sbuf.append("(define (find-firstn alist n)" + _endl);
        _sbuf.append("    (cond" + _endl);
        _sbuf.append("        ((= n 0) ())" + _endl);
        _sbuf.append("        (else (cons (car alist) (find-firstn (cdr alist) (- n 1))))" + _endl);
        _sbuf.append("))" + _endl);
        _sbuf.append("(define (delistify alist)" + _endl);
        _sbuf.append("(cond" + _endl);
        _sbuf.append("        ((null? alist) ())" + _endl);
        _sbuf.append("        ((list? (car alist))" + _endl);
        _sbuf.append("                (append (delistify (car alist)) (delistify (cdr alist))))" + _endl);
        _sbuf.append("        (else (cons (car alist) (delistify (cdr alist))))))" + _endl);
        return _sbuf.toString();
    }
}
