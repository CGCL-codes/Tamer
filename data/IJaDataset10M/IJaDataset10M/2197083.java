package tests4;

import com.curl.io.serialize.types.annotation.DefaultNotNull;
import com.curl.io.serialize.types.annotation.Nullable;

@DefaultNotNull
public class NonNullService2 {

    public String genNonNull1() {
        return "";
    }

    public Person getNonNull2() {
        return new Person("", 1);
    }

    @Nullable
    public String genNull1() {
        return "";
    }

    @Nullable
    public Person getNull2() {
        return new Person("", 1);
    }
}
