package org.authorsite.web.tags;

import java.util.Locale;
import junit.framework.*;
import org.authorsite.web.JspContextStub;
import org.authorsite.web.MockJspWriter;

/**
 *
 * @author jejking
 */
public class PagingTagTest extends TestCase {

    private PagingTag pagingTag;

    private MockJspWriter mockJspWriter;

    public PagingTagTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        this.pagingTag = new PagingTag();
        this.mockJspWriter = new MockJspWriter(1, false);
        JspContextStub jspContext = new JspContextStub();
        jspContext.setMockJspWriter(this.mockJspWriter);
        this.pagingTag.setJspContext(jspContext);
    }

    protected void tearDown() throws Exception {
    }

    public void testSanitizeInputsLowCount() {
        this.pagingTag.setCount(-1);
        this.pagingTag.sanitizeInputs();
        assertEquals(0, this.pagingTag.getCount());
        assertEquals(1, this.pagingTag.getMaxPageNumber());
    }

    public void testSanitizeInputsLowPageNumber1() {
        this.pagingTag.setPageNumber(-1);
        this.pagingTag.sanitizeInputs();
        assertEquals(1, this.pagingTag.getPageNumber());
        assertEquals(1, this.pagingTag.getMaxPageNumber());
    }

    public void testSanitizeInputsLowPageNumber2() {
        this.pagingTag.setPageNumber(0);
        this.pagingTag.sanitizeInputs();
        assertEquals(1, this.pagingTag.getPageNumber());
        assertEquals(1, this.pagingTag.getMaxPageNumber());
    }

    public void testSanitizeInputsLowPageSize() {
        this.pagingTag.setPageSize(-2);
        this.pagingTag.sanitizeInputs();
        assertEquals(10, this.pagingTag.getPageSize());
    }

    public void testSanitizeInputsHighPageSize() {
        this.pagingTag.setPageSize(456);
        this.pagingTag.sanitizeInputs();
        assertEquals(10, this.pagingTag.getPageSize());
    }

    public void testCalcMaxPageNumberExactDivision() {
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.sanitizeInputs();
        assertEquals(10, this.pagingTag.getMaxPageNumber());
    }

    public void testCalcMaxPageNumberWithRemainderOnLastpage() {
        this.pagingTag.setCount(105);
        this.pagingTag.setPageSize(10);
        this.pagingTag.sanitizeInputs();
        assertEquals(11, this.pagingTag.getMaxPageNumber());
    }

    public void testCalcMaxPageNumberOnlyOnePage() {
        this.pagingTag.setCount(9);
        this.pagingTag.setPageSize(10);
        this.pagingTag.sanitizeInputs();
        assertEquals(1, this.pagingTag.getMaxPageNumber());
    }

    public void testCurrentPageNumberDoesNotExceedMaxPageNumber() {
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(30);
        this.pagingTag.sanitizeInputs();
        assertEquals(10, this.pagingTag.getMaxPageNumber());
        assertEquals(10, this.pagingTag.getPageNumber());
    }

    public void testDoFirstIsFirstPage() throws Exception {
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doFirst(this.mockJspWriter);
        assertEquals("<div class=\"pager\"><span id=\"pager.first\"> &lt;&lt; </span>", this.mockJspWriter.getPrinted());
    }

    public void testDoFirstSecondPage() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(2);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doFirst(this.mockJspWriter);
        assertEquals("<div class=\"pager\"><span id=\"pager.first\"><a href=\"/test/index?pageNumber=1&pageSize=10\" title=\"First\"> &lt;&lt; </a></span>", this.mockJspWriter.getPrinted());
    }

    public void testDoPreviousIsFirstPage() throws Exception {
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doPrevious(this.mockJspWriter);
        assertEquals("<span id=\"pager.previous\"> &lt; </span>", this.mockJspWriter.getPrinted());
    }

    public void testDoPreviousThirdPage() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(3);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doPrevious(this.mockJspWriter);
        assertEquals("<span id=\"pager.previous\"><a href=\"/test/index?pageNumber=2&pageSize=10\" title=\"Previous\"> &lt; </a></span>", this.mockJspWriter.getPrinted());
    }

    public void testDoNextFirstAndOnlyPage() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(10);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doNext(this.mockJspWriter);
        assertEquals("<span id=\"pager.next\"> &gt; </span>", this.mockJspWriter.getPrinted());
    }

    public void testDoNextLastPage() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(10);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doNext(this.mockJspWriter);
        assertEquals("<span id=\"pager.next\"> &gt; </span>", this.mockJspWriter.getPrinted());
    }

    public void testDoNextSecondOfThree() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(30);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(2);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doNext(this.mockJspWriter);
        assertEquals("<span id=\"pager.next\"><a href=\"/test/index?pageNumber=3&pageSize=10\" title=\"Next\"> &gt; </a></span>", this.mockJspWriter.getPrinted());
    }

    public void testDoLastFirstAndOnlyPage() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(10);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doLast(this.mockJspWriter);
        assertEquals("<span id=\"pager.last\"> &gt;&gt; </span></div>", this.mockJspWriter.getPrinted());
    }

    public void testDoLastLastOfThree() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(30);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(3);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doLast(this.mockJspWriter);
        assertEquals("<span id=\"pager.last\"> &gt;&gt; </span></div>", this.mockJspWriter.getPrinted());
    }

    public void testDoLastPageOneOfThree() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(30);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doLast(this.mockJspWriter);
        assertEquals("<span id=\"pager.last\"><a href=\"/test/index?pageNumber=3&pageSize=10\" title=\"Last\"> &gt;&gt; </a></span></div>", this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockOneOnly() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(10);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockOneOfTwo() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(20);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockTwoOfTwo() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(20);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(2);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockOneOfFive() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(50);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(1);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockTwoOfFive() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(50);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(2);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockThreeOfFive() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(50);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(3);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockFourOfFive() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(50);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(4);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockFiveOfFive() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(50);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(5);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockSevenOfTen() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(7);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockNineOfTen() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(9);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }

    public void testDoCentralBlockTenOfTen() throws Exception {
        this.pagingTag.setLocale(Locale.ENGLISH);
        this.pagingTag.setCount(100);
        this.pagingTag.setPageSize(10);
        this.pagingTag.setPageNumber(10);
        this.pagingTag.setIndexUrl("/test/index");
        this.pagingTag.loadResourceBundle();
        this.pagingTag.sanitizeInputs();
        this.pagingTag.doCentralBlock(this.mockJspWriter);
        System.out.println(this.mockJspWriter.getPrinted());
    }
}
