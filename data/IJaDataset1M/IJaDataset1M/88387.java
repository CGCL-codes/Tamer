package org.databene.benerator.demo;

import org.databene.benerator.Generator;
import org.databene.benerator.sample.ConstantGenerator;
import org.databene.benerator.util.GeneratorUtil;
import org.databene.benerator.distribution.SequenceManager;
import org.databene.benerator.engine.DefaultBeneratorContext;
import org.databene.benerator.factory.StochasticGeneratorFactory;
import org.databene.benerator.file.FileBuilder;
import org.databene.benerator.wrapper.MultiSourceArrayGenerator;
import org.databene.benerator.wrapper.WrapperFactory;
import org.databene.commons.*;
import org.databene.commons.converter.FormatFormatConverter;
import org.databene.commons.format.Alignment;
import org.databene.document.fixedwidth.ArrayFixedWidthWriter;
import org.databene.document.fixedwidth.FixedWidthColumnDescriptor;
import org.databene.model.data.Uniqueness;
import org.databene.script.AbstractScript;
import java.io.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.math.BigDecimal;

/**
 * Demonstrates the creation of fixed column width files from an array generator.<br/>
 * <br/>
 * Created: 07.06.2007 12:04:39
 * @author Volker Bergmann
 */
public class ArrayFixedWidthDemo {

    private static final String FILE_NAME = "persons.fcw";

    private static final int LENGTH = 5;

    public static void main(String[] args) throws IOException {
        Writer out = null;
        try {
            FixedWidthColumnDescriptor[] descriptors = new FixedWidthColumnDescriptor[] { new FixedWidthColumnDescriptor("rowType", 1, Alignment.RIGHT), new FixedWidthColumnDescriptor("recordNumber", 8, Alignment.RIGHT), new FixedWidthColumnDescriptor("type", 4, Alignment.LEFT), new FixedWidthColumnDescriptor("date", 8, Alignment.LEFT), new FixedWidthColumnDescriptor("partner", 6, Alignment.LEFT), new FixedWidthColumnDescriptor("articleNumber", 6, Alignment.RIGHT), new FixedWidthColumnDescriptor("itemCount", 3, Alignment.LEFT), new FixedWidthColumnDescriptor("itemPrice", 6, Alignment.LEFT) };
            out = new OutputStreamWriter(System.out);
            HeaderScript headerScript = new HeaderScript(LENGTH);
            DocumentWriter<Object[]> writer = new ArrayFixedWidthWriter<Object>(out, headerScript, null, descriptors);
            System.out.println("Running...");
            long startMillis = System.currentTimeMillis();
            TransactionGenerator generator = new TransactionGenerator();
            FileBuilder.build(generator, LENGTH, writer);
            long elapsedTime = System.currentTimeMillis() - startMillis;
            System.out.println("Created file " + FILE_NAME + " with " + LENGTH + " entries " + "within " + (elapsedTime / 1000) + "s (" + (LENGTH * 1000L / elapsedTime) + " entries per second)");
        } finally {
            IOUtil.close(out);
        }
    }

    public static class TransactionGenerator extends MultiSourceArrayGenerator<Object> {

        public TransactionGenerator() {
            super(Object.class, false, createSources());
        }

        @SuppressWarnings({ "unchecked", "cast" })
        private static Generator<Object>[] createSources() {
            StochasticGeneratorFactory generatorFactory = new StochasticGeneratorFactory();
            Generator<Date> dateGenerator = generatorFactory.createDateGenerator(TimeUtil.date(2004, 0, 1), TimeUtil.date(2006, 11, 31), Period.DAY.getMillis(), SequenceManager.RANDOM_SEQUENCE);
            FormatFormatConverter<Date> dateRenderer = new FormatFormatConverter<Date>(Date.class, new SimpleDateFormat("yyyyMMdd"), false);
            Generator<Object>[] sources = (Generator<Object>[]) new Generator[] { new ConstantGenerator<String>("R"), generatorFactory.createNumberGenerator(Integer.class, 1, true, LENGTH, true, 1, SequenceManager.RANDOM_WALK_SEQUENCE, Uniqueness.NONE), generatorFactory.createSampleGenerator(CollectionUtil.toList("BUY", "SALE"), String.class, false), WrapperFactory.applyConverter(dateGenerator, dateRenderer), generatorFactory.createSampleGenerator(CollectionUtil.toList("Alice", "Bob", "Charly"), String.class, false), generatorFactory.createRegexStringGenerator("[A-Z0-9]{6}", 6, 6, Uniqueness.NONE), generatorFactory.createNumberGenerator(Integer.class, 1, true, 20, true, 1, SequenceManager.RANDOM_SEQUENCE, Uniqueness.NONE), generatorFactory.createNumberGenerator(BigDecimal.class, new BigDecimal("0.50"), true, new BigDecimal("99.99"), true, new BigDecimal("0.01"), SequenceManager.CUMULATED_SEQUENCE, Uniqueness.NONE) };
            GeneratorUtil.initAll(sources, new DefaultBeneratorContext());
            return sources;
        }
    }

    private static class HeaderScript extends AbstractScript {

        int length;

        public HeaderScript(int length) {
            this.length = length;
        }

        @Override
        public void execute(Context context, Writer writer) throws IOException {
            writer.write("H");
            writer.write(StringUtil.padRight("Tx", 12, ' '));
            writer.write(StringUtil.padLeft(String.valueOf(length), 8, ' '));
            writer.write(new SimpleDateFormat("yyyyMMdd").format(new Date()));
            writer.write(SystemInfo.getLineSeparator());
        }
    }
}
