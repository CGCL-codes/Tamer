package ch.ethz.mxquery.MinimalConformance.Functions;

import ch.ethz.mxquery.testsuite.XQueryTestBase;
import ch.ethz.mxquery.testsuite.XQueryTestCase;
import ch.ethz.mxquery.query.PreparedStatement;
import ch.ethz.mxquery.contextConfig.Context;
import ch.ethz.mxquery.TestResourceManager;
import ch.ethz.mxquery.exceptions.MXQueryException;

public class ErrorFuncclass extends XQueryTestBase {

    public void test_fn_error_1() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-1 :)\n(: Description: Evaluation of \"fn:error\" function with no arguments :)\n\nfn:error() \n").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_2() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-2 :)\n(: Description: Evaluation of \"fn:error\" function as per example 2 from the Funcs. and Ops. Specifications. :)\n\nfn:error(fn:QName('http://www.example.com/HR', 'myerr:toohighsal'), 'Does not apply because salary is too high')\n").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "*" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart()) || errorcode.equals("*")) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_3() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-3 :)\n(: Description: Evaluation of \"fn:error\" function with wrong argument type. :)\n\nfn:error('Wrong Argument Type')\n").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_4() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-4 :)\n(: Description: Evaluation of \"fn:error\" for error code \"FOCH0004\". :)\n\n fn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:FOCH0004')) \n").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOCH0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_5() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-5 :)\n(: Description: Evaluation of \"fn:error\" with first argument set to empty string for 3rd signature. :)\n\n fn:error((), 'err:FOER0000') \n").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_6() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-6 :)\n(: Description: Evaluation of \"fn:error\" with first argument set to empty string for 4rd signature. :)\n\n fn:error((), 'err:FOER0000','error raised by this test by setting first argument to empty sequence') \n").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_7() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-7 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"FODT0001\". :)\n\n fn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:FODT0001')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FODT0001" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_8() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-8 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"FORG0009\". :)\n\n fn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:FORG0009')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FORG0009" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_9() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-9 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"FOTY0012\". :)\n\n fn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:FOTY0012')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOTY0012" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_10() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-10 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SENR0001\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SENR0001')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SENR0001" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_11() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-11 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SEPM0004\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SEPM0004')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SEPM0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_12() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-12 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SEPM0009\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SEPM0009')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SEPM0009" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_13() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-13 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SEPM0010\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SEPM0010')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SEPM0010" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_14() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-14 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SEPM0016\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SEPM0016')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SEPM0016" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_15() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-15 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SERE0003\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SERE0003')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SERE0003" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_16() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-16 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SERE0005\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SERE0005')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SERE0005" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_17() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-17 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SERE0006\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SERE0006')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SERE0006" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_18() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-18 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SERE0008\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SERE0008')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SERE0008" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_19() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-19 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SERE0012\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SERE0012')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SERE0012" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_20() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-20 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SERE0014\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SERE0014')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SERE0014" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_22() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-22 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SESU0007\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SESU0007')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SESU0007" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_23() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-23 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"SESU0011\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:SESU0011')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "SESU0011" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_25() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-25 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XPDY0002\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XPDY0002')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPDY0002" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_26() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-26 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XPST0010\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XPST0010')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPST0010" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_27() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-27 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XPST0080\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XPST0080')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPST0080" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_28() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-28 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XPTY0018\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XPTY0018')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0018" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_29() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-29 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQDY0027\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQDY0027')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQDY0027" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_30() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-30 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQDY0061\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQDY0061')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQDY0061" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_31() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-31 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQDY0084\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQDY0084')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQDY0084" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_32() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-32 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0009\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0009')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0009" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_33() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-33 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0012\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0012')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0012" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_34() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-34 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0013\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0013')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0013" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_35() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-35 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0016\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0016')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0016" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_36() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-36 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0035\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0035')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0035" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_37() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-37 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0036\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0036')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0036" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_38() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-38 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0046\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0046')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0046" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_39() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-39 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0047\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0047')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0047" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_40() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-40 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0048\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0048')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0048" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_41() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-41 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0054\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0054')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0054" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_42() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-42 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0055\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0055')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0055" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_43() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-43 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0057\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0057')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0057" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_44() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-44 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0058\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0058')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0058" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_45() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-45 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0060\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0060')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0060" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_46() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-46 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0073. :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0073')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0073" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_47() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-47 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0075\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0075')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0075" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_48() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-48 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0076\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0076')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0076" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_49() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-49 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0079\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0079')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0079" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_50() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-50 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQST0087\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQST0087')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQST0087" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_fn_error_51() throws Exception {
        try {
            String query = new StringBuilder().append("(: Name: fn-error-51 :)\n(: Description: Evaluation of \"fn:error\" set to raise error \"XQTY0030\". :)\n\nfn:error(fn:QName('http://www.w3.org/2005/xqt-errors', 'err:XQTY0030')) ").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context1", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XQTY0030" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ErrorFunc_1() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-1                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `if(true()) then true() else error()`. :)\n(:*******************************************************:)\nif(true()) then true() else error()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_ErrorFunc_2() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-2                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `if(true()) then true() else error(QName(\"\", \"local\"), \"description\")`. :)\n(:*******************************************************:)\nif(true()) then true() else \n						error(QName(\"\", \"local\"), \"description\")").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_ErrorFunc_3() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-3                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `error(QName(\"\", \"local\"), \"description\", \"object\", \"wrong param\")`. :)\n(:*******************************************************:)\nerror(QName(\"\", \"local\"), \n						\"description\", \"object\", \"wrong param\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPST0017" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ErrorFunc_4() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-4                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `error( () )`.      :)\n(:*******************************************************:)\nerror( () )").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "XPTY0004" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ErrorFunc_5() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-5                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `error(QName('http://www.w3.org/2005/xqt-errors', 'err:FOER0000'))`. :)\n(:*******************************************************:)\nerror(QName('http://www.w3.org/2005/xqt-errors',\n						'err:FOER0000'))").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ErrorFunc_6() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-6                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: When fn:error() is passed a description, the first argument may be an empty sequence. :)\n(:*******************************************************:)\nerror((), \"description\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ErrorFunc_7() throws Exception {
        String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-7                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `if(false()) then error((), \"description\") else true()`. :)\n(:*******************************************************:)\nif(false()) then error((), \"description\") else true()").toString();
        XQueryTestCase testcase = new XQueryTestCase();
        testcase.addVariable("input-context", "emptydoc", true);
        testcase.execute(query);
        assertXMLEqual("Text", new String[] { new StringBuilder().append("true").toString() }, testcase.result());
    }

    ;

    public void test_K_ErrorFunc_8() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-8                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `error()`.          :)\n(:*******************************************************:)\nerror()").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ErrorFunc_9() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-9                                   :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `error(QName(\"\", \"XPDY6666\"), \"description\", \"error object\")`. :)\n(:*******************************************************:)\nerror(QName(\"\", \"XPDY6666\"), \n						\"description\", \"error object\")").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "*" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart()) || errorcode.equals("*")) return;
            }
            assertTrue(false);
        }
    }

    ;

    public void test_K_ErrorFunc_10() throws Exception {
        try {
            String query = new StringBuilder().append("(:*******************************************************:)\n(: Test: K-ErrorFunc-10                                  :)\n(: Written by: Frans Englich                             :)\n(: Date: 2006-10-05T18:29:39+02:00                       :)\n(: Purpose: A test whose essence is: `exactly-one((true(), error()))`. :)\n(:*******************************************************:)\nexactly-one((true(), error()))").toString();
            XQueryTestCase testcase = new XQueryTestCase();
            testcase.addVariable("input-context", "emptydoc", true);
            testcase.execute(query, true);
            assertTrue(false);
        } catch (MXQueryException ex) {
            String[] errorcodes = { "FOER0000" };
            for (String errorcode : errorcodes) {
                if (errorcode.equals(ex.getErrorCode().getLocalPart())) return;
            }
            assertTrue(false);
        }
    }

    ;
}
