package biz.evot.util.lang;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

/**
 * <p>
 * 코드 블럭, 클로져의 구현체. ( 참고 : http://www.jdl.co.uk/briefings/closures.html )
 * </p>
 *
 * <p>
 * Closure는 독립적으로 실행가능한 코드 영역을 나타낸다.클로져 자체만으로는 별다른 역활을 하지 못하고 다양한 Support class에
 * 의해 동작한다.
 * </p>
 * <p>
 * 간단한 예제를 하나 기술하자면,<br>
 *
 * <code>
 *
 * List users = new ArrayList();
 * ...
 * NCollection.each(user, new Closure() {
 *     public void doWith(Object obj) {
 *         User user  = (User) obj;
 *         user.calcRevenu();
 *     }
 * });
 *
 * </code>
 *
 * 처럼 사용할수 있다.
 * </p>
 *
 * <p>
 * 클로져에서 제공하는 기능것은 크게 doWith, execute, test, collect가 있다.
 * <ul>
 * <li> doWith는 이미 지정된 File, In/OutputStream, Reader, Writer등의 객체를 사용하기 위해
 * 지정되었다. doWith는 NFile, NInputStrea, NOutputStrea, NReader, NWriter등의 support
 * class와 동작한다. <br>
 * <li> execute, test, collect는 알반적인 collection, array와 동작하기 위해 만들어 졌다. <br>
 * <li> execute는 NCollection의 each 메소드에서 사용된다.<br>
 * <li> test는 NCollection의 every, find, findAll 메소드에서 사용된다.<br>
 * <li> collect는 NCollection의 collect메소드에서 사용된다. . <br>
 * </ul>
 * </p>
 *
 * @author narusas
 *
 */
public class Closure {

    public void doWith(File file) {
    }

    public void doWith(String line) {
    }

    public void doWith(BufferedReader reader) {
    }

    public void doWith(InputStream in) {
    }

    public void doWith(OutputStream out) {
    }

    public void doWith(PrintWriter out) {
    }

    public void doWith(Reader r) {
    }

    public void doWith(Writer w) {
    }

    public void execute(Object param) {
    }

    public void execute(Object key, Object value) {
    }

    public void execute(byte b) {
    }

    public void execute(byte[] b) {
    }

    public void execute(short s) {
    }

    public void execute(int i) {
    }

    public void execute(long l) {
    }

    public void execute(double d) {
    }

    public void execute(float f) {
    }

    public void execute(char obj) {
    }

    public void execute(boolean bool) {
    }

    public boolean test(Object obj) {
        return false;
    }

    public boolean test(Object key, Object value) {
        return false;
    }

    public boolean test(byte b) {
        return false;
    }

    public boolean test(short s) {
        return false;
    }

    public boolean test(int i) {
        return false;
    }

    public boolean test(long l) {
        return false;
    }

    public boolean test(float f, float tolerance) {
        return false;
    }

    public boolean test(double d, double tolerance) {
        return false;
    }

    public boolean test(char i) {
        return false;
    }

    public boolean test(boolean bool) {
        return false;
    }

    public Object collect(Object object) {
        return null;
    }

    public Object collect(Object key, Object value) {
        return null;
    }
}
