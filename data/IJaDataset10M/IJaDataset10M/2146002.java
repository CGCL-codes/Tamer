package de.andreavicentini.magiclogger.service;

import java.io.File;
import org.magiclabs.basix.Magics;

public interface ILogEntry {

    enum Category {

        INFO, ERROR, FIXABLE
    }

    Category getCategory();

    String getMessage();

    File getSource();

    class Default implements ILogEntry {

        private final Category category;

        private final String message;

        private final File source;

        public Default(Category category, File source, String message) {
            this.category = category;
            this.source = source;
            this.message = message;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof ILogEntry)) return false;
            ILogEntry other = (ILogEntry) obj;
            return Magics.equalsHelper(this.getCategory(), other.getCategory()) && Magics.equalsHelper(this.getSource(), other.getSource()) && Magics.equalsHelper(this.getMessage(), other.getMessage());
        }

        @Override
        public int hashCode() {
            return Magics.hashCodeHelper(new Object[] { this.category, this.source, this.message });
        }

        @Override
        public Category getCategory() {
            return this.category;
        }

        @Override
        public String getMessage() {
            return this.message;
        }

        @Override
        public File getSource() {
            return this.source;
        }
    }

    class Info extends Default {

        public Info(File source, String message) {
            super(Category.INFO, source, message);
        }
    }

    class Error extends Default {

        public Error(File source, String message) {
            super(Category.ERROR, source, message);
        }
    }

    class Fixable extends Default {

        private final ISolution solution;

        public Fixable(File source, String message, ISolution solution) {
            super(Category.FIXABLE, source, message);
            this.solution = solution;
        }

        public void solve() throws Exception {
            this.solution.solve();
        }
    }
}
