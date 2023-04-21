package mscheme.tests;

import mscheme.exceptions.CompileError;
import mscheme.exceptions.ImmutableException;
import mscheme.exceptions.SchemeException;
import mscheme.exceptions.SyntaxArityError;
import mscheme.values.Function;
import mscheme.values.ScmString;
import mscheme.values.ScmVector;
import mscheme.values.ValueTraits;

public class TestR5RS extends TestSchemeBase {

    public static final String CVS_ID = "$Id: TestR5RS.java 724 2004-12-28 13:37:07Z sielenk $";

    public TestR5RS(String name) {
        super(name);
    }

    public void test4_1_1() throws SchemeException, InterruptedException {
        eval("(define x 28)");
        check("x", "28");
    }

    public void test4_1_2() throws SchemeException, InterruptedException {
        check("(quote a)", "a");
        check("(quote #(a b c))", "#(a b c)");
        check("(quote (+ 1 2))", "(+ 1 2)");
        check("'a", "a");
        check("'#(a b c)", "#(a b c)");
        check("'()", "()");
        check("'(+ 1 2)", "(+ 1 2)");
        check("'(quote a)", "(quote a)");
        check("''a", "(quote a)");
        check("'\"abc\"", "\"abc\"");
        check("\"abc\"", "\"abc\"");
        check("'145932", "145932");
        check("145932", "145932");
        check("'#t", "#t");
        check("#t", "#t");
        try {
            ValueTraits.toMutablePair(eval("'(1 . 2)")).setFirst(quote("a"));
            fail();
        } catch (ImmutableException e) {
        }
        try {
            ((ScmString) eval("'\"abc\"")).set(0, 'b');
            fail();
        } catch (ImmutableException e) {
        }
        try {
            ((ScmVector) eval("'#(1 2 3)")).set(0, quote("a"));
            fail();
        } catch (ImmutableException e) {
        }
    }

    public void test4_1_3() throws SchemeException, InterruptedException {
        check("(+ 3 4)", "7");
        check("((if #f + *) 3 4)", "12");
        try {
            eval("()");
            fail();
        } catch (CompileError e) {
        }
    }

    public void test4_1_4() throws SchemeException, InterruptedException {
        assertTrue(eval("(lambda (x) (+ x x))") instanceof Function);
        check("((lambda (x) (+ x x)) 4)", "8");
        eval("(define reverse-subtract\n" + "  (lambda (x y) (- y x)))");
        check("(reverse-subtract 7 10)", "3");
        eval("(define add4\n" + "  (let ((x 4))\n" + "    (lambda (y) (+ x y))))");
        check("(add4 6)", "10");
        try {
            eval("(lambda (x y x) y)");
            fail();
        } catch (CompileError e) {
        }
        check("((lambda x x) 3 4 5 6)", "(3 4 5 6)");
        check("((lambda (x y .z) z) 3 4 5 6)", "(5 6)");
    }

    public void test4_1_5() throws SchemeException, InterruptedException {
        check("(if (> 3 2) 'yes 'no)", "yes");
        check("(if (> 2 3) 'yes 'no)", "no");
        check("(if (> 3 2)\n" + "    (- 3 2)\n" + "    (+ 3 2))", "1");
    }

    public void test4_1_6() throws SchemeException, InterruptedException {
        eval("(define x 2)");
        check("(+ x 1)", "3");
        eval("(set! x 4)");
        check("(+ x 1)", "5");
    }

    public void no_test4_2_1_cond() throws SchemeException, InterruptedException {
        check("(cond ((> 3 2) 'greater)\n" + "      ((< 3 2) 'less))", "greater");
        check("(cond ((> 3 3) 'greater)\n" + "      ((< 3 3) 'less))\n" + "      (else 'equal))", "equal");
        check("(cond ((assv 'b '((a 1) (b 2))) => cadr)\n" + "      (else #f))", "2");
    }

    public void test4_2_1_and() throws SchemeException, InterruptedException {
        check("(and (= 2 2) (> 2 1))", "#t");
        check("(and (= 2 2) (< 2 1))", "#f");
        check("(and 1 2 'c 'f '(f g))", "(f g)");
        check("(and)", "#t");
    }

    public void test4_2_1_or() throws SchemeException, InterruptedException {
        check("(or (= 2 2) (> 2 1))", "#t");
        check("(or (= 2 2) (< 2 1))", "#t");
        check("(or (memq 'b '(a b c)) (/ 3 0))", "(b c)");
        check("(or)", "#f");
    }

    public void test4_2_2_let() throws SchemeException, InterruptedException {
        check("(let ((x 2) (y 3))\n" + "  (* x y))", "6");
        check("(let ((x 2) (y 3))\n" + "  (let ((x 7)\n" + "        (z (+ x y)))\n" + "    (* z x)))", "35");
        try {
            eval("(let ((x 1) (y x)) 0)");
            fail();
        } catch (SchemeException e) {
        }
        try {
            eval("(let ((x y) (y 1)) 0)");
            fail();
        } catch (SchemeException e) {
        }
    }

    public void test4_2_2_letstar() throws SchemeException, InterruptedException {
        check("(let ((x 2) (y 3))\n" + "  (let* ((x 7)\n" + "         (z (+ x y)))\n" + "    (* z x)))", "70");
        check("(let* ((a 1) (b (+ a 1)) (c (+ a b)) (d (+ b c)) (e (+ c d)))" + "  (list a b c d e))", "(1 2 3 5 8)");
        check("(let* ((x 1) (y x)) y)", "1");
        try {
            eval("(let* ((x y) (y 1)) 0)");
            fail();
        } catch (SchemeException e) {
        }
    }

    public void test4_2_2_letrec() throws SchemeException, InterruptedException {
        check("(letrec ((even?\n" + "          (lambda (n)\n" + "            (if (zero? n)\n" + "                #t\n" + "                (odd? (- n 1)))))\n" + "         (odd?\n" + "          (lambda (n)\n" + "            (if (zero? n)\n" + "                #f\n" + "                (even? (- n 1))))))\n" + "  (even? 88))\n", "#t");
        check("(letrec ((x (lambda () y)) (y (lambda () x))) 0)", "0");
    }

    public void test4_2_2_common() throws SchemeException, InterruptedException {
        check("(let ((x 1)) (let    () (define x 2)) x)", "1");
        check("(let ((x 1)) (let*   () (define x 2)) x)", "1");
        check("(let ((x 1)) (letrec () (define x 2)) x)", "1");
    }

    public void test4_2_3() throws SchemeException, InterruptedException {
        eval("(define x 0)");
        check("(begin (set! x 5)\n" + "       (+ x 1))", "6");
    }

    public void test4_2_4_named_let() throws SchemeException, InterruptedException {
        check("(let f ((x 3) (y 7))\n" + "  (if (= x 0) y (f (- x 1) y)))", "7");
    }

    public void notest4_2_6() throws SchemeException, InterruptedException {
        check("`(list ,(+ 1 2) 4)", "(list 3 4)");
        check("(let ((name 'a)) `(list ,name ',name))", "(list a (quote a))");
        check("`(a ,(+ 1 2) ,@(list 4 5 6) b)", "(a 3 4 5 6 b)");
        check("`#(10 5 ,(+ 1 1) ,@(list 4 3) 8)", "#(10 5 2 4 3 8)");
        check("`(a `(b ,(+ 1 2) ,(foo ,(+ 1 3) d) e) f)", "(a `(b ,(+ 1 2) ,(foo 4 d) e) f)");
        check("(let ((name1 'x) (name2 'y)) `(a `(b ,,name1 ,',name2 d) e))", "(a `(b ,x ,'y d) e))");
    }

    public void test5_2() throws SchemeException, InterruptedException {
        try {
            eval("(define x 1 2 3)");
            fail();
        } catch (SyntaxArityError e) {
        }
        try {
            eval("(set! x 1 2 3)");
            fail();
        } catch (SyntaxArityError e) {
        }
        check("(begin (define (f x) x x x x x) (f 1))", "1");
        try {
            check("(let ((a 1))" + "  (define (f x)" + "    (define b (+ a x))" + "    (define a 5)" + "    (+ a b))" + "  (f 10))", "20");
        } catch (SchemeException e) {
        }
    }

    public void test5_3() throws SchemeException, InterruptedException {
        eval("(define-syntax static-cons " + "  (lambda (def-env use-env . args)" + "    (cons " + "      use-env " + "      (list 'quote (apply cons args)))))");
        check("(static-cons a b)", "(a . b)");
    }

    public void test6_1() throws SchemeException, InterruptedException {
        check("(eqv? 'a 'a)", "#t");
        check("(eqv? 'a 'b)", "#f");
        check("(eqv? 2 2)", "#t");
        check("(eqv? '() '())", "#t");
        check("(eqv? (cons 1 2) (cons 1 2))", "#f");
        check("(eqv? (lambda () 1) (lambda () 2))", "#f");
        check("(eqv? #f 'nil)", "#f");
        check("(let ((p (lambda (x) x))) (eqv? p p))", "#t");
    }

    public void testNumbers() throws SchemeException, InterruptedException {
        check("(+ 3 4)", "7");
        check("(+ 3)", "3");
        check("(+)", "0");
        check("(* 4)", "4");
        check("(*)", "1");
        check("(+)", "0");
        check("(*)", "1");
        check("(- 1)", "-1");
        check("(/ 1)", "1");
        check("(+ 3 3)", "6");
        check("(- 3 3)", "0");
        check("(* 3 3)", "9");
        check("(/ 3 3)", "1");
        check("(+  1 2 3 4)", "10");
        check("(-  1 2 3 4)", "-8");
        check("(*  1 2 3 4)", "24");
        check("(/ 24 2 3 4)", "1");
    }

    public void test6_3_2() throws SchemeException, InterruptedException {
        check("(pair? '(a . b))", "#t");
        check("(pair? '(a b c))", "#t");
        check("(pair? '())", "#f");
        check("(pair? '#(a b))", "#f");
        check("(cons 'a '())", "(a)");
        check("(cons '(a) '(b c d))", "((a) b c d)");
        check("(cons \"a\" '(b c))", "(\"a\" b c)");
        check("(cons 'a '3)", "(a . 3)");
        check("(cons '(a b) 'c)", "((a b) . c)");
        check("(car '(a b c))", "a");
        check("(car '((a) b c d))", "(a)");
        check("(car '(1 . 2))", "1");
        try {
            eval("(car '())");
            fail();
        } catch (SchemeException e) {
        }
        check("(cdr '((a) b c d))", "(b c d)");
        check("(cdr '(1 . 2))", "2");
        try {
            eval("(cdr '())");
            fail();
        } catch (SchemeException e) {
        }
        eval("(define (f) (list 'not-a-constant-list))");
        eval("(define (g) '(constant-list))");
        eval("(set-car! (f) 3)");
        try {
            eval("(set-car! (g) 3)");
            fail();
        } catch (SchemeException e) {
        }
        eval("(set-cdr! (f) 3)");
        try {
            eval("(set-cdr! (g) 3)");
            fail();
        } catch (SchemeException e) {
        }
        check("(list? '(a b c))", "#t");
        check("(list? '())", "#t");
        check("(list? '(a . b))", "#f");
        check("(list 'a (+ 3 4) 'c)", "(a 7 c)");
        check("(list)", "()");
        check("(length '(a b c))", "3");
        check("(length '(a (b) (c d e)))", "3");
        check("(length '())", "0");
        check("(append '(x) '(y))", "(x y)");
        check("(append '(a) '(b c d))", "(a b c d)");
        check("(append '(a (b)) '((c)))", "(a (b) (c))");
        check("(append '(a b) '(c . d))", "(a b c . d)");
        check("(append '() 'a)", "a");
        check("(reverse '(a b c))", "(c b a)");
        check("(reverse '(a (b c) d (e (f))))", "((e (f)) d (b c) a)");
        check("(memq 'a '(a b c))", "(a b c)");
        check("(memq 'b '(a b c))", "(b c)");
        check("(memq 'a '(b c d))", "#f");
        check("(memq   (list 'a) '(b (a) c))", "#f");
        check("(member (list 'a) '(b (a) c))", "((a) c)");
        check("(memv '101 '(100 101 102))", "(101 102)");
        eval("(define e '((a 1) (b 2) (c 3)))");
        check("(assq 'a e)", "(a 1)");
        check("(assq 'b e)", "(b 2)");
        check("(assq 'd e)", "#f");
        check("(assq  (list 'a) '(((a)) ((b)) ((c))))", "#f");
        check("(assoc (list 'a) '(((a)) ((b)) ((c))))", "((a))");
        check("(assv 5 '((2 3) (5 7) (11 13)))", "(5 7)");
        check("(assq 'b '((a . 1) (b . 2)))", "(b . 2)");
    }

    public void test6_3_3() throws SchemeException, InterruptedException {
        check("(symbol? 'foo)", "#t");
        check("(symbol? (car '(a b)))", "#t");
        check("(symbol? \"bar\")", "#f");
        check("(symbol? 'nil)", "#t");
        check("(symbol? '())", "#f");
        check("(symbol? #f)", "#f");
        check("(symbol->string 'flying-fish)", "\"flying-fish\"");
        check("(symbol->string 'Martin)", "\"martin\"");
        check("(symbol->string (string->symbol \"Malvia\"))", "\"Malvia\"");
    }

    public void test6_3_5() throws SchemeException, InterruptedException {
        eval("(define (f) (make-string 3 #\\*))");
        eval("(define (g) \"***\"))");
        eval("(string-set! (f) 0 #\\?)");
        try {
            eval("(string-set! (g) 0 #\\?)");
            fail();
        } catch (SchemeException e) {
        }
        try {
            eval("(string-set! (symbol->string 'immutable) 0 #\\?)");
            fail();
        } catch (SchemeException e) {
        }
    }

    public void test6_3_6() throws SchemeException, InterruptedException {
        check("(vector-ref '#(1 1 2 3 5 8 13 21) 5)", "8");
        check("(vector 'a 'b 'c)", "#(a b c)");
        check("(vector-length '#(1 2 3 4))", "4");
    }

    public void test6_4_procedureq() throws SchemeException, InterruptedException {
        check("(procedure?  car)", "#t");
        check("(procedure? 'car)", "#f");
        check("(procedure?  (lambda (x) (* x x)))", "#t");
        check("(procedure? '(lambda (x) (* x x)))", "#f");
        check("(call-with-current-continuation procedure?)", "#t");
    }

    public void test6_4_apply() throws SchemeException, InterruptedException {
        check("(apply + (list 3 4))", "7");
        check("(apply + 1 2 '(3 4))", "10");
        check("(let ((l (list 1 2)))" + "  (apply" + "    (lambda x (set-car! x 3))" + "    l)" + "  l)", "(1 2)");
    }

    public void test6_4_map() throws SchemeException, InterruptedException {
        check("(map + '(1 2 3) '(4 5 6))", "(5 7 9)");
    }

    public void test6_4_for_each() throws SchemeException, InterruptedException {
        eval("(define count 0)");
        check("(for-each" + "  (lambda (x y)" + "    (set! count (+ count 1))" + "    (+ x y count))" + "  '(1 2 3)" + "  '(4 5 6))", "(6 9 12)");
    }

    public void test6_4_force() throws SchemeException, InterruptedException {
        check("(force (delay (+ 1 2)))", "3");
        check("(let ((p (delay (+ 1 2)))) (list (force p) (force p)))", "(3 3)");
        eval("(begin" + "  (define count 0)" + "  (define x 'foo)" + "  (define p" + "    (delay" + "      (begin" + "        (set! count (+ count 1))" + "        (if (> count x)" + "          count" + "          (force p)))))" + "  (define x 5))");
        check("(force p)", "6");
        eval("(set! x 10)");
        check("(force p)", "6");
    }

    public void test6_4_callcc() throws SchemeException, InterruptedException {
        check("(let ((path '())\n" + "      (c #f))\n" + "  (let ((add (lambda (s)\n" + "               (set! path (cons s path)))))\n" + "    (dynamic-wind\n" + "      (lambda () (add 'connect))\n" + "      (lambda ()\n" + "        (add (call-with-current-continuation\n" + "               (lambda (c0)\n" + "                 (set! c c0)\n" + "                 'talk1))))\n" + "      (lambda () (add 'disconnect)))\n" + "    (if (< (length path) 4)\n" + "        (c 'talk2)\n" + "        (reverse path))))", "(connect talk1 disconnect\n" + " connect talk2 disconnect)");
    }

    public void test6_5() throws SchemeException, InterruptedException {
        check("(eval '(* 7 3) (scheme-report-environment 5))", "21");
        check("(let ((f (eval '(lambda (f x) (f x x))\n" + "       (null-environment 5))))\n" + "  (f + 10))", "20");
    }

    public void testSpawn() throws Exception {
        check("(spawn (lambda (c) 7))", "7");
        check("(cons " + "1 " + "(spawn " + "(lambda (c) " + "(cons " + "2 " + "(c " + "(lambda (k) " + "(cons " + "3 " + "(k '()))))))))", "(1 3 2)");
        try {
            eval("((spawn (lambda (c) c)) (lambda (k) k))");
            fail();
        } catch (SchemeException e) {
        }
        try {
            eval("(spawn (lambda (c) " + "(c (lambda (k) " + "(c (lambda (k) k))))))");
            fail();
        } catch (SchemeException e) {
        }
        eval("(define id " + "(spawn (lambda (c) " + "(c (c (lambda (k) " + "(k (lambda (k) k))))))))");
        check("(id  45)", "45");
        check("(id '())", "()");
    }
}
