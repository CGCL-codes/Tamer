package javaframework.base;

import java.util.Calendar;
import javaframework.base.exceptions.FrameworkRuntimeException;

/**
 * Defines base methods to reflect the properties of the running object.
 * <p/>
 * <br/><br/>
 * <p/>
 * <b><u>Dependencies</u></b><br/> None <br/><br/>
 * <p/>
 * <b><u>Design notes</u></b><br/>
 * <p/>
 * <br/><br/>
 * <p/>
 * <b>· Creation time: 01/0/2007</b><br/> <b>· Revisions:</b> - <br/><br/> <b><u>State</u></b><br/> <b>· Debugged: </b> Yes<br/> <b>· Structural tests: </b> -<br/> <b>· Functional tests: </b> -<br/>
 * <p/>
 * @author Francisco Pérez R. de V. (franjfw{@literal @}yahoo.es)
 * @version JavaFramework.0.1.0.en
 * @version ClassTracker.0.0.1.en
 * @since JavaFramework.0.1.0.en
 * @see <a href=””></a>
 * <p/>
 */
public class ClassTracker extends AbstractClassReleasingResources implements InterfaceClassTracker {

    private Calendar dateTime;

    private String runningMethod;

    private String className;

    private String fileName;

    private int lineNumber;

    private boolean nativeMethod;

    private String summaryString;

    private String extraInfo;

    private Object[] parameterValues;

    private void setRunningMethod(final String runningMethod) {
        this.runningMethod = runningMethod;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public String getRunningMethod() {
        return this.runningMethod;
    }

    private void setClassName(final String className) {
        this.className = className;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public String getClassName() {
        return this.className;
    }

    private void setFileName(final String fileName) {
        this.fileName = fileName;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public String getFileName() {
        return this.fileName;
    }

    private void setLineNumber(final int lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public int getLineNumber() {
        return this.lineNumber;
    }

    private void setNativeMethod(final boolean isNativeMethod) {
        this.nativeMethod = isNativeMethod;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public boolean isNativeMethod() {
        return this.nativeMethod;
    }

    private void setSummaryString(final String summaryString) {
        this.summaryString = summaryString;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public String getSummaryString() {
        return this.summaryString;
    }

    private void setExtraInfo(final String extraInfo) {
        this.extraInfo = extraInfo;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public String getExtraInfo() {
        return this.extraInfo;
    }

    private void setParameterValues(final Object... parameterValues) {
        this.parameterValues = parameterValues;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public Object[] getParameterValues() {
        return this.parameterValues;
    }

    private void setDateTime(final Calendar calendar) {
        this.dateTime = calendar;
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public Calendar getDateTime() {
        return this.dateTime;
    }

    public ClassTracker(final TrackedClass trackedClass, final String extraInfo, final Object... parameterValues) {
        final int INDEX_STACK_TRACE;
        switch(trackedClass) {
            case THIS_CLASS:
                INDEX_STACK_TRACE = 2;
                break;
            case CALLER_CLASS:
                INDEX_STACK_TRACE = 3;
                break;
            default:
                final String EXCEPTION_MSG = "TrackedClass parameter is not valid.";
                throw new FrameworkRuntimeException(EXCEPTION_MSG, trackedClass, extraInfo, parameterValues);
        }
        this.setDateTime(Calendar.getInstance());
        this.setRunningMethod(Thread.currentThread().getStackTrace()[INDEX_STACK_TRACE].getMethodName());
        this.setClassName(Thread.currentThread().getStackTrace()[INDEX_STACK_TRACE].getClassName());
        this.setFileName(Thread.currentThread().getStackTrace()[INDEX_STACK_TRACE].getFileName());
        this.setLineNumber(Thread.currentThread().getStackTrace()[INDEX_STACK_TRACE].getLineNumber());
        this.setNativeMethod(Thread.currentThread().getStackTrace()[INDEX_STACK_TRACE].isNativeMethod());
        this.setSummaryString(Thread.currentThread().getStackTrace()[INDEX_STACK_TRACE].toString());
        this.setExtraInfo(extraInfo);
        this.setParameterValues(parameterValues);
    }

    /**
	 * @{@inheritDoc }
	 */
    @Override
    public void releaseResources() {
        this.setDateTime(null);
        this.setRunningMethod(null);
        this.setClassName(null);
        this.setFileName(null);
        this.setSummaryString(null);
        this.setExtraInfo(null);
        for (int i = 0; i < this.getParameterValues().length; i++) {
            this.getParameterValues()[i] = null;
        }
        this.parameterValues = null;
    }
}
