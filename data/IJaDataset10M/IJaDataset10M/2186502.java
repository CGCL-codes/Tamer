package book;

public class TestBook {

    IBook book;

    TestBook(IBook book) {
        this.book = book;
    }

    public static void main(String args[]) {
        IBook book;
        String[] authors = { "Borhes" };
        book = new ConcreteBook("Stories", authors, 1985, 2, " ");
        TestBook tb = new TestBook(book);
        System.out.println(tb.printValues());
    }

    private String printValues() {
        return book.returnBookName() + "\t" + book.returnBookAuthors() + "\n\t" + book.returnYear() + "\t" + book.returnVersion() + ", " + book.returnISBN();
    }
}
