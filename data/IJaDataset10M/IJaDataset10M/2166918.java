package com.netx.basic.R1.eh;

import java.io.IOException;
import java.io.CharConversionException;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.InterruptedIOException;
import java.io.ObjectStreamException;
import java.io.SyncFailedException;
import java.io.UnsupportedEncodingException;
import java.io.UTFDataFormatException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.net.URISyntaxException;
import java.net.SocketTimeoutException;
import java.sql.SQLException;
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.IllegalFormatException;
import java.util.ConcurrentModificationException;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.EmptyStackException;
import java.util.InvalidPropertiesFormatException;
import java.util.TooManyListenersException;
import com.netx.basic.R1.shared.Globals;
import com.netx.basic.R1.l10n.L10n;

public class ErrorHandler {

    public static String getMessage(Throwable t) {
        Checker.checkNull(t, "t");
        if (t instanceof Translated) {
            return t.getMessage();
        } else if (t instanceof ArithmeticException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ARITHMETIC);
        } else if (t instanceof IndexOutOfBoundsException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INDEX_OUT_OF_BOUNDS);
        } else if (t instanceof ArrayStoreException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CLASS_CAST);
        } else if (t instanceof ClassCastException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CLASS_CAST);
        } else if (t instanceof NumberFormatException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_NUMBER_FORMAT);
        } else if (t instanceof IllegalFormatException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ILLEGAL_FORMAT);
        } else if (t instanceof IllegalArgumentException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ILLEGAL_ARG);
        } else if (t instanceof IllegalMonitorStateException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ILLEGAL_MONITOR_STATE);
        } else if (t instanceof IllegalStateException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ILLEGAL_STATE);
        } else if (t instanceof NegativeArraySizeException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ILLEGAL_ARG);
        } else if (t instanceof NullPointerException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_NULL_POINTER);
        } else if (t instanceof SecurityException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_SECURITY);
        } else if (t instanceof TypeNotPresentException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CLASS_CAST);
        } else if (t instanceof UnsupportedOperationException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_UNSUPPORTED_OPERATION);
        } else if (t instanceof ConcurrentModificationException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CONCURRENT_MODIFICATION);
        } else if (t instanceof EmptyStackException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INDEX_OUT_OF_BOUNDS);
        } else if (t instanceof NoSuchElementException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INDEX_OUT_OF_BOUNDS);
        } else if (t instanceof MissingResourceException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_MISSING_RESOURCE);
        } else if (t instanceof BufferOverflowException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_BUFFER_OVERFLOW);
        } else if (t instanceof BufferUnderflowException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_BUFFER_UNDERFLOW);
        } else if (t instanceof ValidationException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_VALIDATION);
        } else if (t instanceof IllegalUsageException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ILLEGAL_USAGE);
        } else if (t instanceof IntegrityException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INTEGRITY);
        } else if (t instanceof ClassNotFoundException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CLASS_NOT_FOUND);
        } else if (t instanceof CloneNotSupportedException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CLONE_NOT_SUPPORTED);
        } else if (t instanceof EnumConstantNotPresentException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CLASS_NOT_FOUND);
        } else if (t instanceof IllegalAccessException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_ILLEGAL_ACCESS);
        } else if (t instanceof InstantiationException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INSTANTIATION);
        } else if (t instanceof InterruptedException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INTERRUPTED);
        } else if (t instanceof NoSuchFieldException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_NO_SUCH_FIELD);
        } else if (t instanceof NoSuchMethodException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_NO_SUCH_METHOD);
        } else if (t instanceof InvalidPropertiesFormatException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INVALID_PROPERTIES_FORMAT);
        } else if (t instanceof CharConversionException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CHAR_CONVERSION);
        } else if (t instanceof EOFException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_EOF);
        } else if (t instanceof FileNotFoundException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_FILE_NOT_FOUND);
        } else if (t instanceof SocketTimeoutException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_SOCKET_TIMEOUT);
        } else if (t instanceof InterruptedIOException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_INTERRUPTED_IO);
        } else if (t instanceof ObjectStreamException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_OBJECT_STREAM);
        } else if (t instanceof NoSuchMethodException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_NO_SUCH_METHOD);
        } else if (t instanceof SyncFailedException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_SYNC_FAILED);
        } else if (t instanceof UnsupportedEncodingException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_UNSUPPORTED_ENCODING);
        } else if (t instanceof UTFDataFormatException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_UTF_DATA_FORMAT);
        } else if (t instanceof BindException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_BIND);
        } else if (t instanceof ConnectException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_CONNECT);
        } else if (t instanceof NoRouteToHostException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_NO_ROUTE_TO_HOST);
        } else if (t instanceof PortUnreachableException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_PORT_UNREACHABLE);
        } else if (t instanceof HttpRetryException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_HTTP_RETRY);
        } else if (t instanceof MalformedURLException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_MALFORMED_URL);
        } else if (t instanceof ProtocolException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_PROTOCOL);
        } else if (t instanceof UnknownHostException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_UNKNOWN_HOST);
        } else if (t instanceof UnknownServiceException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_UNKNOWN_SERVICE);
        } else if (t instanceof IOException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_IO);
        } else if (t instanceof TooManyListenersException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_TOO_MANY_LISTENERS);
        } else if (t instanceof URISyntaxException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_URI_SYNTAX);
        } else if (t instanceof SQLException) {
            return L10n.getContent(L10n.BASIC_MSG_EH_SQL);
        } else if (t instanceof ExceptionInInitializerError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_EXCEPTION_IN_INITIALIZER);
        } else if (t instanceof NoClassDefFoundError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_NO_CLASS_DEF_FOUND);
        } else if (t instanceof UnsatisfiedLinkError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_UNSATISFIED_LINK);
        } else if (t instanceof LinkageError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_LINKAGE);
        } else if (t instanceof InternalError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_VM_INTERNAL);
        } else if (t instanceof OutOfMemoryError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_VM_OUT_OF_MEMORY);
        } else if (t instanceof StackOverflowError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_VM_STACK_OVERFLOW);
        } else if (t instanceof UnknownError) {
            return L10n.getContent(L10n.BASIC_MSG_EH_VM_UNKNOWN);
        } else {
            Globals.getLogger().warn("could not find functional error message for unknown exception " + t);
            return L10n.getContent(L10n.BASIC_MSG_EH_UNKNOWN);
        }
    }
}
