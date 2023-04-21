package org.knott.kadavr.metadata.attr;

import java.io.IOException;
import org.knott.kadavr.metadata.ClassFileReader;
import org.knott.kadavr.metadata.ConstPool;

/**
 * Аттрибут, который по спецификации Java хранит
 * в себе Java байткод.
 * @author Sergey
 */
public class Code extends Attribute {

    public static final String NAME = "Code";

    private int maxLocals, maxStack;

    private byte[] code;

    private ExceptionEntry[] exceptions;

    private Attributes attributes;

    /**
     * Возвратить массив с байткодом
     * реализующим метод, аттрибутом которого
     * является данный экземпляр.
     * @return
     */
    public byte[] getCode() {
        return code;
    }

    /**
     * Возратить количество локальных
     * слотов определённых в данном
     * методе.
     * @return
     */
    public int getMaxLocals() {
        return maxLocals;
    }

    /**
     * Возратить количество слотов
     * стека необходимых для выполнения данного
     * метода.
     * @return
     */
    public int getMaxStack() {
        return maxStack;
    }

    /**
     * Возратить количество записей
     * в таблице исключений данного
     * аттрибута.
     */
    public int getExceptionCount() {
        return exceptions.length;
    }

    /**
     * Возратить запись из таблицы исключений.
     */
    public ExceptionEntry getException(int i) {
        return exceptions[i];
    }

    public Attributes getAttributes() {
        return attributes;
    }

    @Override
    public void read(ConstPool pool, ClassFileReader dis) throws IOException {
        maxStack = dis.readU2();
        maxLocals = dis.readU2();
        long codeLength = dis.readU4();
        code = new byte[(int) codeLength];
        dis.read(code);
        int exceptionsLength = dis.readU2();
        exceptions = new ExceptionEntry[exceptionsLength];
        for (int i = 0; i < exceptionsLength; i++) {
            ExceptionEntry entry = new ExceptionEntry();
            entry.read(dis);
            exceptions[i] = entry;
        }
        AttributeReader reader = new AttributeReader(pool);
        attributes = reader.read(dis);
    }

    @Override
    public String getName() {
        return NAME;
    }

    /**
     * Класс представляющий собой запись
     * в таблице исключений в аттрибуте <i>Code</i>.
     */
    public static class ExceptionEntry {

        private int startPC, endPC;

        private int handlerPC;

        private int catchType;

        ExceptionEntry(int startPC, int endPC, int handlerPC, int catchType) {
            this.startPC = startPC;
            this.endPC = endPC;
            this.handlerPC = handlerPC;
            this.catchType = catchType;
        }

        public ExceptionEntry() {
        }

        /**
         * Считать данные из потока.
         * @param dis
         * @throws IOException Если возникнет ошибка ввода вывода.
         */
        public void read(ClassFileReader dis) throws IOException {
            startPC = dis.readU2();
            endPC = dis.readU2();
            handlerPC = dis.readU2();
            catchType = dis.readU2();
        }

        public int getCatchType() {
            return catchType;
        }

        public int getEndPC() {
            return endPC;
        }

        public int getHandlerPC() {
            return handlerPC;
        }

        public int getStartPC() {
            return startPC;
        }
    }
}
