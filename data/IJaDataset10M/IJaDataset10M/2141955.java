package library.database.mybatis.common;

import library.enums.Library;

public interface SignatureGenerator {

    String generate(Library library);
}
