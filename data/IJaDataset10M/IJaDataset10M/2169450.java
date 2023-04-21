package quickcheck;

import net.java.quickcheck.Generator;
import net.java.quickcheck.generator.PrimitiveGenerators;

/**
 * A class to represent an Author of the book
 * @author Virag Shah
 * @since 15 February 2011
 */
public class Author {

    /** the name of the author */
    private String name;

    /** the age of the author */
    private int age;

    /** The standard full constructor
	 * @param name the name of the author
	 * @param age the age of the author
	 */
    public Author(String name, int age) {
        this.name = name;
        this.age = age;
    }

    /**
	 * 
	 * @return
	 */
    public String getName() {
        return name;
    }

    /**
	 * 
	 * @return
	 */
    public int getAge() {
        return age;
    }

    /**
	 * 
	 */
    public boolean equals(Object o) {
        Author a = (Author) o;
        return a.name.equals(this.name) && a.age == this.age;
    }
}

/**
 * 
 * @author Virag
 *
 */
class AuthorGenerator implements net.java.quickcheck.Generator<Author> {

    Generator<String> name = PrimitiveGenerators.strings();

    Generator<Integer> age = PrimitiveGenerators.integers(0, 125);

    /**
	 * 
	 */
    @Override
    public Author next() {
        return new Author(name.next(), age.next());
    }
}
