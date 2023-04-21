package com.rapidminer.operator.io;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import com.rapidminer.gui.tools.dialogs.wizards.dataimport.csv.CSVImportWizard.CSVDataReaderWizardCreator;
import com.rapidminer.gui.tools.dialogs.wizards.dataimport.csv.LineReader;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.UserError;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeBoolean;
import com.rapidminer.parameter.ParameterTypeChar;
import com.rapidminer.parameter.ParameterTypeConfiguration;
import com.rapidminer.parameter.ParameterTypeFile;
import com.rapidminer.parameter.ParameterTypeString;
import com.rapidminer.parameter.conditions.BooleanParameterCondition;
import com.rapidminer.tools.CSVParseException;
import com.rapidminer.tools.DateParser;
import com.rapidminer.tools.LineParser;
import com.rapidminer.tools.StrictDecimalFormat;
import com.rapidminer.tools.io.Encoding;

/**
 * A reader for CSV files.
 * 
 * @author Tobias Malbrecht, Sebastian Loh
 */
public class CSVDataReader extends AbstractDataReader {

    public static final String PARAMETER_CSV_FILE = "file_name";

    static {
        AbstractReader.registerReaderDescription(new ReaderDescription("csv", CSVDataReader.class, PARAMETER_CSV_FILE));
    }

    public static final String PARAMETER_USE_FIRST_ROW_AS_ATTRIBUTE_NAMES = "use_first_row_as_attribute_names";

    public static final String PARAMETER_TRIM_LINES = "trim_lines";

    public static final String PARAMETER_SKIP_COMMENTS = "skip_comments";

    public static final String PARAMETER_COMMENT_CHARS = "comment_characters";

    public static final String PARAMETER_USE_QUOTES = "use_quotes";

    public static final String PARAMETER_QUOTES_CHARACTER = "quotes_character";

    public static final String PARAMETER_COLUMN_SEPARATORS = "column_separators";

    public static final String PARAMETER_ESCAPE_CHARACTER = "escape_character_for_quotes";

    public CSVDataReader(OperatorDescription description) {
        super(description);
        getParameters().addObserver(new CacheResetParameterObserver(PARAMETER_CSV_FILE), false);
    }

    @Override
    protected DataSet getDataSet() throws OperatorException {
        return new DataSet() {

            private boolean firstRowAreNames = getParameterAsBoolean(PARAMETER_USE_FIRST_ROW_AS_ATTRIBUTE_NAMES);

            private LineReader reader = null;

            private String[] parsedLine = null;

            private final LineParser parser = new LineParser();

            {
                parser.setTrimLine(getParameterAsBoolean(PARAMETER_TRIM_LINES));
                parser.setSkipComments(getParameterAsBoolean(PARAMETER_SKIP_COMMENTS));
                parser.setSplitExpression(getParameterAsString(PARAMETER_COLUMN_SEPARATORS));
                parser.setUseQuotes(getParameterAsBoolean(PARAMETER_USE_QUOTES));
                parser.setQuoteCharacter(getParameterAsChar(PARAMETER_QUOTES_CHARACTER));
                parser.setQuoteEscapeCharacter(getParameterAsChar(PARAMETER_ESCAPE_CHARACTER));
                parser.setCommentCharacters(getParameterAsString(PARAMETER_COMMENT_CHARS));
                parser.setEncoding(Encoding.getEncoding(CSVDataReader.this));
                InputStream stream = null;
                try {
                    stream = getParameterAsInputStream(PARAMETER_CSV_FILE);
                } catch (IOException e) {
                    throw new UserError(CSVDataReader.this, e, 302, stream, e.getMessage());
                }
                reader = new LineReader(stream, Encoding.getEncoding(CSVDataReader.this));
                if (firstRowAreNames && !CSVDataReader.this.attributeNamesDefinedByUser()) {
                    String line = null;
                    try {
                        do {
                            line = reader.readLine();
                            if (line == null) {
                                break;
                            }
                            try {
                                parsedLine = parser.parse(line);
                            } catch (IllegalArgumentException e) {
                                parsedLine = new String[] { line };
                            }
                        } while (parsedLine == null);
                        setAttributeNames(parsedLine);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    if (firstRowAreNames) {
                        try {
                            reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            private final NumberFormat numberFormat = StrictDecimalFormat.getInstance(CSVDataReader.this, true);

            private final DateFormat dateFormat = DateParser.getInstance(CSVDataReader.this);

            @Override
            public boolean next() {
                String line = null;
                try {
                    do {
                        line = reader.readLine();
                        if (line == null) {
                            return false;
                        }
                        try {
                            parsedLine = parser.parse(line);
                        } catch (CSVParseException e) {
                            if (!isErrorTolerant()) {
                                throw new IllegalArgumentException(e);
                            } else {
                                parsedLine = new String[] { line };
                            }
                        }
                    } while (parsedLine == null);
                    return true;
                } catch (IOException e) {
                    return false;
                }
            }

            @Override
            public int getNumberOfColumnsInCurrentRow() {
                return parsedLine.length;
            }

            @Override
            public boolean isMissing(int columnIndex) {
                return parsedLine[columnIndex] == null || parsedLine[columnIndex].isEmpty();
            }

            @Override
            public Number getNumber(int columnIndex) {
                if (numberFormat == null) {
                    return null;
                }
                try {
                    return numberFormat.parse(parsedLine[columnIndex].replace('e', 'E'));
                } catch (ParseException e) {
                }
                return null;
            }

            @Override
            public String getString(int columnIndex) {
                return parsedLine[columnIndex];
            }

            @Override
            public Date getDate(int columnIndex) {
                try {
                    return dateFormat.parse(parsedLine[columnIndex]);
                } catch (ParseException e) {
                }
                return null;
            }

            @Override
            public void close() throws OperatorException {
                try {
                    reader.close();
                } catch (IOException e) {
                }
            }
        };
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = new LinkedList<ParameterType>();
        ParameterType type = new ParameterTypeConfiguration(CSVDataReaderWizardCreator.class, this);
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeFile(PARAMETER_CSV_FILE, "Name of the file to read the data from.", "csv", false));
        types.addAll(Encoding.getParameterTypes(this));
        types.add(new ParameterTypeBoolean(PARAMETER_TRIM_LINES, "Indicates if lines should be trimmed (empty spaces are removed at the beginning and the end) before the column split is performed. This option might be problematic if TABs are used as a seperator.", false));
        types.add(new ParameterTypeBoolean(PARAMETER_SKIP_COMMENTS, "Indicates if a comment character should be used.", true));
        type = new ParameterTypeString(PARAMETER_COMMENT_CHARS, "Lines beginning with these characters are ignored.", "#", true);
        type.registerDependencyCondition(new BooleanParameterCondition(this, PARAMETER_SKIP_COMMENTS, false, true));
        types.add(type);
        type = new ParameterTypeBoolean(PARAMETER_USE_FIRST_ROW_AS_ATTRIBUTE_NAMES, "Read attribute names from file (assumes the attribute names are in the first line of the file).", true);
        type.setExpert(false);
        types.add(type);
        types.add(new ParameterTypeBoolean(PARAMETER_USE_QUOTES, "Indicates if quotes should be regarded.", true));
        type = new ParameterTypeChar(PARAMETER_QUOTES_CHARACTER, "The quotes character.", LineParser.DEFAULT_QUOTE_CHARACTER, true);
        type.registerDependencyCondition(new BooleanParameterCondition(this, PARAMETER_USE_QUOTES, false, true));
        types.add(type);
        type = new ParameterTypeChar(PARAMETER_ESCAPE_CHARACTER, "The charcter that is used to escape quotes", LineParser.DEFAULT_QUOTE_ESCAPE_CHARACTER, true);
        type.registerDependencyCondition(new BooleanParameterCondition(this, PARAMETER_USE_QUOTES, false, true));
        types.add(type);
        types.add(new ParameterTypeString(PARAMETER_COLUMN_SEPARATORS, "Column separators for data files (regular expression)", ";"));
        types.addAll(StrictDecimalFormat.getParameterTypes(this, true));
        types.addAll(DateParser.getParameterTypes(this));
        types.addAll(super.getParameterTypes());
        return types;
    }
}
