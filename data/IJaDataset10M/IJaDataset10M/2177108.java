package ru.dpelevin.gddc.command;

import ru.dpelevin.command.ApplicationCommand;
import ru.dpelevin.gddc.model.Language;

/**
 * Detects default language pair for translation.
 * 
 * @author Dmitry Pelevin
 */
@SuppressWarnings("serial")
public class DetectDefaultInputLanguagesCommand extends ApplicationCommand<DetectDefaultInputLanguagesCommand.Result> {

    /**
	 * Instantiates a new detect default input languages command.
	 * 
	 * @param source
	 *            the source
	 */
    public DetectDefaultInputLanguagesCommand(final Object source) {
        super(source);
    }

    /**
	 * The Class Result.
	 */
    public static class Result {

        /** The source language. */
        private final Language sourceLanguage;

        /** The destination language. */
        private final Language destinationLanguage;

        /**
		 * Instantiates a new result.
		 * 
		 * @param sourceLanguage
		 *            the source language
		 * @param destinationLanguage
		 *            the destination language
		 */
        public Result(final Language sourceLanguage, final Language destinationLanguage) {
            this.sourceLanguage = sourceLanguage;
            this.destinationLanguage = destinationLanguage;
        }

        /**
		 * Gets the source language.
		 * 
		 * @return the source language
		 */
        public final Language getSourceLanguage() {
            return sourceLanguage;
        }

        /**
		 * Gets the destination language.
		 * 
		 * @return the destination language
		 */
        public final Language getDestinationLanguage() {
            return destinationLanguage;
        }
    }
}
