package com.netx.ut.basic.R1;

import java.io.IOException;
import java.io.CharConversionException;
import java.io.EOFException;
import java.io.FileNotFoundException;
import java.io.InterruptedIOException;
import java.io.InvalidClassException;
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
import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.util.ConcurrentModificationException;
import java.util.MissingResourceException;
import java.util.NoSuchElementException;
import java.util.EmptyStackException;
import java.util.InvalidPropertiesFormatException;
import java.util.TooManyListenersException;
import java.sql.SQLException;
import com.netx.generics.R1.util.UnitTester;
import com.netx.basic.R1.eh.ErrorHandler;
import com.netx.basic.R1.eh.IllegalUsageException;
import com.netx.basic.R1.eh.IntegrityException;
import com.netx.basic.R1.eh.ObjectAlreadyExistsException;
import com.netx.basic.R1.eh.ObjectNotFoundException;
import com.netx.basic.R1.eh.ValidationException;
import com.netx.basic.R1.l10n.L10n;
import com.netx.basic.R1.logging.Logger;

public class NTErrorHandling extends UnitTester {

    public static void main(String[] args) throws Throwable {
        final NTErrorHandling nt = new NTErrorHandling();
        nt.t01_ShowUnexpectedMessage();
        nt.t02_ErrorHandlerMsgs();
    }

    public void t01_ShowUnexpectedMessage() {
        Throwable t = new OutOfMemoryError();
        println(L10n.getContent(L10n.GLOBAL_MSG_UNEXPECTED_ERROR, ErrorHandler.getMessage(t)));
    }

    public void t02_ErrorHandlerMsgs() {
        _print(new Exception());
        _print(new ArithmeticException());
        _print(new IndexOutOfBoundsException());
        _print(new ArrayIndexOutOfBoundsException());
        _print(new StringIndexOutOfBoundsException());
        _print(new ArrayStoreException());
        _print(new ClassCastException());
        _print(new IllegalArgumentException());
        _print(new NumberFormatException());
        _print(new IllegalMonitorStateException());
        _print(new IllegalStateException());
        _print(new NegativeArraySizeException());
        _print(new NullPointerException());
        _print(new SecurityException());
        _print(new TypeNotPresentException("MyType", new Exception()));
        _print(new UnsupportedOperationException());
        _print(new ConcurrentModificationException());
        _print(new EmptyStackException());
        _print(new NoSuchElementException());
        _print(new MissingResourceException("", "", ""));
        _print(new BufferOverflowException());
        _print(new BufferUnderflowException());
        _print(new ObjectAlreadyExistsException("folder1", L10n.GLOBAL_WORD_FOLDER));
        _print(new ObjectNotFoundException("folder2", L10n.GLOBAL_WORD_FOLDER));
        _print(new ValidationException(""));
        _print(new IllegalUsageException(""));
        _print(new IntegrityException());
        _print(new ClassNotFoundException());
        _print(new CloneNotSupportedException());
        _print(new EnumConstantNotPresentException(Logger.LEVEL.class, ""));
        _print(new IllegalAccessException());
        _print(new InstantiationException());
        _print(new InterruptedException());
        _print(new NoSuchFieldException());
        _print(new NoSuchMethodException());
        _print(new IOException());
        _print(new InvalidPropertiesFormatException(""));
        _print(new CharConversionException());
        _print(new EOFException());
        _print(new FileNotFoundException());
        _print(new InterruptedIOException());
        _print(new SocketTimeoutException());
        _print(new InvalidClassException(""));
        _print(new SyncFailedException(""));
        _print(new UnsupportedEncodingException());
        _print(new UTFDataFormatException());
        _print(new BindException());
        _print(new ConnectException());
        _print(new NoRouteToHostException());
        _print(new PortUnreachableException());
        _print(new HttpRetryException("", 0));
        _print(new MalformedURLException());
        _print(new ProtocolException());
        _print(new UnknownHostException());
        _print(new UnknownServiceException());
        _print(new TooManyListenersException());
        _print(new URISyntaxException("", ""));
        _print(new SQLException());
        _print(new LinkageError());
        _print(new ExceptionInInitializerError());
        _print(new NoClassDefFoundError());
        _print(new UnsatisfiedLinkError());
        _print(new VerifyError());
        _print(new InternalError());
        _print(new OutOfMemoryError());
        _print(new StackOverflowError());
        _print(new UnknownError());
        _print(new UnknownError());
    }

    private void _print(Throwable t) {
        println(t.getClass().toString() + ": " + ErrorHandler.getMessage(t));
    }
}
