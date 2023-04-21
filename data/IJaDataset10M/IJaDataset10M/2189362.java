package uni.compilerbau.tabelle;

import uni.compilerbau.wrapper.Feld;
import uni.compilerbau.wrapper.Konstruktor;
import uni.compilerbau.wrapper.Methode;

public class ExceptionHandle {

    @Feld
    private final int start;

    @Feld
    private final int end;

    @Feld
    private final int catchPos;

    @Feld
    private final Class exceptionClass;

    @Konstruktor
    public ExceptionHandle(int start, int end, int catchPos, Class exceptionClass) {
        this.start = start;
        this.end = end;
        this.catchPos = catchPos;
        this.exceptionClass = exceptionClass;
        return;
    }

    @Konstruktor
    public ExceptionHandle(ExceptionHandle exceptionHandle, int offset) {
        this.start = exceptionHandle.start + offset;
        this.end = exceptionHandle.end + offset;
        this.catchPos = exceptionHandle.catchPos + offset;
        this.exceptionClass = exceptionHandle.exceptionClass;
        return;
    }

    @Methode
    public int getStart() {
        return this.start;
    }

    @Methode
    public int getEnd() {
        return this.end;
    }

    @Methode
    public int getCatchPos() {
        return this.catchPos;
    }

    @Methode
    public Class getExceptionClass() {
        return this.exceptionClass;
    }
}
