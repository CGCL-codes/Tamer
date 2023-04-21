package ru.util;

import ru.util.Pair;

public interface TextPrinter {

    public TextPrinter keyWord(String text);

    public TextPrinter print(String text);

    public TextPrinter print(String text, TextStyle style);

    public TextPrinter print(Object obj);

    public TextPrinter eol();

    public TextPrinter highlighted(String text);

    public void clear();

    public TextPrinter err(String text);

    public void setKey(Pair<String, String> pair);

    public void intIndent();

    public void decIndent();

    public TextPrinter append(Printable printable);
}
