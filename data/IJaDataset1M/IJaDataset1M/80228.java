package er.ajax.example2.model;

import java.io.File;
import java.io.IOException;
import java.util.Random;
import com.webobjects.eocontrol.EOSortOrdering;
import com.webobjects.foundation.NSArray;
import com.webobjects.foundation.NSMutableArray;
import er.extensions.components.ERXLoremIpsumGenerator;
import er.extensions.foundation.ERXFileUtilities;

public class ExampleDataFactory {

    private static NSMutableArray<Word> _exampleData;

    public static NSMutableArray<Word> randomWords(int count) {
        NSMutableArray<Word> words = new NSMutableArray<Word>();
        Random rand = new Random();
        for (int i = 0; i < count; i++) {
            String s = "";
            int max = 6 + rand.nextInt(6);
            for (int j = 0; j < max; j++) {
                int c = rand.nextInt(26);
                s += Character.toString((char) ('A' + c));
            }
            Word example = new Word(s);
            words.addObject(example);
        }
        return words;
    }

    public static synchronized NSArray<Word> allWords() {
        if (_exampleData == null) {
            _exampleData = new NSMutableArray<Word>();
            File f = new File("/usr/share/dict/words");
            if (f.exists()) {
                try {
                    String words = ERXFileUtilities.stringFromFile(f);
                    String splitWords[] = words.split("\\n");
                    for (int i = 0; i < splitWords.length; i++) {
                        String word = splitWords[i];
                        if (word.length() > 0 && ((i % 20) == 0)) {
                            Word example = new Word(word);
                            _exampleData.addObject(example);
                        }
                    }
                } catch (IOException e) {
                    System.out.println("ExampleUtils.exampleValues: Can't read " + f + ": " + e);
                }
            }
            if (_exampleData.count() == 0) {
                _exampleData.addObjectsFromArray(ExampleDataFactory.randomWords(1000));
            }
            EOSortOrdering.sortArrayUsingKeyOrderArray(_exampleData, new NSArray<EOSortOrdering>(EOSortOrdering.sortOrderingWithKey("name", EOSortOrdering.CompareAscending)));
        }
        return _exampleData.immutableClone();
    }

    public static NSMutableArray<Product> products(int count) {
        NSMutableArray<Product> products = new NSMutableArray<Product>();
        for (int i = 0; i < count; i++) {
            String title = ERXLoremIpsumGenerator.words(1, 5);
            String summary = ERXLoremIpsumGenerator.paragraph();
            Product product = new Product(title, summary);
            products.addObject(product);
        }
        return products;
    }

    public static NSMutableArray<ComplexPerson> family() {
        NSMutableArray<ComplexPerson> people = new NSMutableArray<ComplexPerson>();
        ComplexPerson mike = new ComplexPerson("Mike", 29);
        ComplexPerson kirsten = new ComplexPerson("Kirsten", 29);
        ComplexPerson andrew = new ComplexPerson("Andrew", 2);
        mike.setSpouse(kirsten);
        kirsten.setSpouse(mike);
        mike.setChildren(new NSArray<ComplexPerson>(andrew));
        kirsten.setChildren(new NSArray<ComplexPerson>(andrew));
        people.add(mike);
        people.add(kirsten);
        people.add(andrew);
        return people;
    }

    public static NSMutableArray<Comment> comments(int count) {
        NSMutableArray<Comment> comments = new NSMutableArray<Comment>();
        for (int i = 1; i < count; i++) {
            Comment comment = new Comment();
            comment.setText("This is comment #" + i);
            comments.addObject(comment);
        }
        return comments;
    }

    public static NSMutableArray<String> values(String prefix, int count) {
        NSMutableArray<String> values = new NSMutableArray<String>();
        for (int i = 0; i < count; i++) {
            values.addObject(prefix + i);
        }
        return values;
    }

    public static NSMutableArray<Item> items(String idSuffix, String prefix, int count) {
        NSMutableArray<Item> items = new NSMutableArray<Item>();
        for (int i = 0; i < count; i++) {
            items.addObject(new Item(String.valueOf(i) + idSuffix, prefix + i));
        }
        return items;
    }
}
