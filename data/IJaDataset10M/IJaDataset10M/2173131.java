package net.sf.julie.library;

import org.junit.Assert;
import org.junit.Test;

public class DelayTest extends AbstractLibraryTest {

    @Test
    public void simple() {
        Assert.assertEquals("3", evalToString("(force (delay (+ 1 2)))"));
    }

    @Test
    public void simple2() {
        Assert.assertEquals("15120", evalToString("(define my-promise (delay (* 5 6 7 8 9)))", "(force my-promise)"));
    }

    @Test
    public void laterEval() {
        Assert.assertEquals("(3 3)", evalToString("(let ((p (delay (+ 1 2)))) (list (force p) (force p)))"));
    }

    @Test
    public void laterEvalComplicated() {
        Assert.assertEquals("6", evalToString("(define count 0)", "(define p (delay (begin (set! count (+ count 1)) (if (> count x) count (force p)))))", "(define x 5)", "(force p)", "(begin (set! x 10) (force p))"));
    }

    @Test
    public void laterEvalComplicatedLetrec() {
        String scm1 = "(letrec ((count 0) (p (delay (begin (set! count (+ count 1)) (if (> count x) count (force p))))) (x 5)) (force p) (set! x 10) (force p))";
        Assert.assertEquals("6", evalToString(scm1));
    }

    @Test
    public void complicated() {
        Assert.assertEquals("2", evalToString("(define a-stream (letrec ((next (lambda (n) (cons n (delay (next (+ n 1))))))) (next 0)))", "(define head car)", "(define tail (lambda (stream) (force (cdr stream))))", "(head (tail (tail a-stream)))"));
    }

    @Test
    public void laterEvalRecursive() {
        Assert.assertEquals("3", evalToString("(letrec ((p (delay (if c 3 (begin (set! c #t) (+ (force p) 1))))) (c #f)) (force p))"));
    }
}
