package net.sf.crepido.io;

import java.io.*;
import java.util.*;
import net.sf.crepido.base.*;
import net.sf.crepido.util.Generator;
import net.sf.crepido.util.Seq;

public class FileSelector extends AbstractUnaryPredicate<File> {

    private final AbstractUnaryPredicate<File> innerFilter;

    public FileSelector() {
        this.innerFilter = FileSelector.SELECT_ALL;
    }

    public FileSelector(AbstractUnaryPredicate<File> f) {
        assert (f != null);
        if (f instanceof FileSelector) {
            this.innerFilter = ((FileSelector) f).innerFilter;
        } else {
            this.innerFilter = f;
        }
    }

    public final boolean apply(final File file) {
        return this.innerFilter.apply(file);
    }

    public FileSelector filter(final AbstractUnaryPredicate<File> f) {
        assert (f != null);
        return new FileSelector(this.innerFilter.and(f));
    }

    public FileSelector isFile() {
        return new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

            public boolean apply(final File file) {
                return (file != null && file.isFile());
            }
        }));
    }

    public FileSelector isDirectory() {
        return new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

            public boolean apply(final File file) {
                return (file != null && file.isDirectory());
            }
        }));
    }

    public FileSelector isVisible() {
        return new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

            public boolean apply(final File file) {
                return (file != null && !file.isHidden());
            }
        }));
    }

    public FileSelector isHidden() {
        return new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

            public boolean apply(final File file) {
                return (file != null && file.isHidden());
            }
        }));
    }

    public FileSelector byFileName(final String... values) {
        final List<String> valueList = Arrays.asList(values);
        return new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

            public boolean apply(final File file) {
                return (valueList.contains(file.getName()));
            }
        }));
    }

    public FileSelector byFileName(final AbstractUnaryPredicate<? super String>... fs) {
        FileSelector ret = this;
        if (fs != null && fs.length > 0) {
            ret = new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

                public boolean apply(File file) {
                    boolean ret = false;
                    for (int i = 0; i < fs.length; ++i) {
                        if (fs[i] != null) {
                            ret |= fs[i].apply(file.getName());
                        }
                    }
                    return ret;
                }
            }));
        }
        return ret;
    }

    public FileSelector byPath(final String value) {
        return new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

            public boolean apply(final File file) {
                return (value != null && value.equals(file.getPath()));
            }
        }));
    }

    public FileSelector byPath(final AbstractUnaryPredicate<? super String>... fs) {
        FileSelector ret = this;
        if (fs != null && fs.length > 0) {
            ret = new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

                public boolean apply(File file) {
                    boolean ret = false;
                    for (int i = 0; i < fs.length; ++i) {
                        if (fs[i] != null) {
                            ret |= fs[i].apply(file.getPath());
                        }
                    }
                    return ret;
                }
            }));
        }
        return ret;
    }

    public FileSelector byAbsolutePath(final AbstractUnaryPredicate<? super String>... fs) {
        FileSelector ret = this;
        if (fs != null && fs.length > 0) {
            ret = new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

                public boolean apply(File file) {
                    boolean ret = false;
                    for (int i = 0; i < fs.length; ++i) {
                        if (fs[i] != null) {
                            ret |= fs[i].apply(file.getAbsolutePath());
                        }
                    }
                    return ret;
                }
            }));
        }
        return ret;
    }

    public FileSelector bySize(final AbstractUnaryPredicate<? super Long>... fs) {
        FileSelector ret = this;
        if (fs != null && fs.length > 0) {
            ret = new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

                public boolean apply(File file) {
                    boolean ret = false;
                    for (int i = 0; i < fs.length; ++i) {
                        if (fs[i] != null) {
                            ret |= fs[i].apply(file.length());
                        }
                    }
                    return ret;
                }
            }));
        }
        return ret;
    }

    public FileSelector byModificationDate(final AbstractUnaryPredicate<? super Date>... fs) {
        FileSelector ret = this;
        if (fs != null && fs.length > 0) {
            ret = new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

                public boolean apply(File file) {
                    boolean ret = false;
                    GregorianCalendar cal = new java.util.GregorianCalendar();
                    cal.setTimeInMillis(file.lastModified());
                    Date modificationDate = cal.getTime();
                    for (int i = 0; i < fs.length; ++i) {
                        if (fs[i] != null) {
                            ret |= fs[i].apply(modificationDate);
                        }
                    }
                    return ret;
                }
            }));
        }
        return ret;
    }

    public FileSelector byModificationAge(final AbstractUnaryPredicate<? super Long>... fs) {
        FileSelector ret = this;
        if (fs != null && fs.length > 0) {
            ret = new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

                public boolean apply(File file) {
                    boolean ret = false;
                    for (int i = 0; i < fs.length; ++i) {
                        if (fs[i] != null) {
                            ret |= fs[i].apply(file.lastModified());
                        }
                    }
                    return ret;
                }
            }));
        }
        return ret;
    }

    public FileSelector bySuffix(final String... values) {
        final List<String> valueList = Arrays.asList(values);
        return new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

            public boolean apply(final File file) {
                return (valueList.contains(FileOps.getFileSuffix.apply(file)));
            }
        }));
    }

    public FileSelector bySuffix(final AbstractUnaryPredicate<? super String>... fs) {
        FileSelector ret = this;
        if (fs != null && fs.length > 0) {
            ret = new FileSelector(this.innerFilter.and(new AbstractUnaryPredicate<File>() {

                public boolean apply(File file) {
                    boolean ret = false;
                    for (int i = 0; i < fs.length; ++i) {
                        if (fs[i] != null) {
                            ret |= fs[i].apply(FileOps.getFileSuffix.apply(file));
                        }
                    }
                    return ret;
                }
            }));
        }
        return ret;
    }

    public FileSelector and(final FileSelector... selectors) {
        FileSelector ret = this;
        if (selectors != null && selectors.length > 0) {
            AbstractUnaryPredicate<File> filter = this.innerFilter;
            for (int i = 0; i < selectors.length; ++i) {
                filter = filter.and(selectors[i].innerFilter);
            }
            ret = new FileSelector(filter);
        }
        return ret;
    }

    public FileSelector or(final FileSelector... selectors) {
        FileSelector ret = this;
        if (selectors != null && selectors.length > 0) {
            AbstractUnaryPredicate<File> filter = this.innerFilter;
            for (int i = 0; i < selectors.length; ++i) {
                filter = filter.or(selectors[i].innerFilter);
            }
            ret = new FileSelector(filter);
        }
        return ret;
    }

    public FileSelector xor(final FileSelector... selectors) {
        FileSelector ret = this;
        if (selectors != null && selectors.length > 0) {
            AbstractUnaryPredicate<File> filter = this.innerFilter;
            for (int i = 0; i < selectors.length; ++i) {
                filter = filter.xor(selectors[i].innerFilter);
            }
            ret = new FileSelector(filter);
        }
        return ret;
    }

    public FileSelector negate() {
        return new FileSelector(this.innerFilter.not());
    }

    public FileFilter toFileFilter() {
        return new FileFilter() {

            public boolean accept(File file) {
                return FileSelector.this.innerFilter.apply(file);
            }
        };
    }

    public Seq<File> listDirectory(final File directory) {
        return this.listDirectory(directory, false, null);
    }

    public Seq<File> listDirectory(final File directory, final boolean recursive) {
        return this.listDirectory(directory, recursive, null);
    }

    public Seq<File> listDirectory(final File directory, final boolean recursive, AbstractUnaryPredicate<File> directoryPredicate) {
        assert (directory != null);
        final AbstractUnaryPredicate<File> dirPredicate = (directoryPredicate != null ? directoryPredicate : SELECT_DIRECTORIES);
        Seq<File> ret = Seq.nil;
        if (directory.isDirectory()) {
            if (!recursive) {
                ret = Seq.ofItems(directory.listFiles(this.toFileFilter()));
            } else {
                final AbstractUnaryPredicate<File> filter = new AbstractUnaryPredicate<File>() {

                    public boolean apply(File file) {
                        return (file != null && (file.isDirectory() || dirPredicate.apply(file)));
                    }
                };
                final File[] files = directory.listFiles(new FileSelector(filter).toFileFilter());
                final int fileCount = (files == null ? 0 : files.length);
                if (fileCount > 0) {
                    ret = Seq.of(new Iterable<File>() {

                        public Iterator<File> iterator() {
                            return new Generator<File>() {

                                private int idx = 0;

                                private Iterator<File> itr = null;

                                public void generate() {
                                    if (this.idx < fileCount || this.itr != null) {
                                        if (this.itr != null) {
                                            if (!this.itr.hasNext()) {
                                                this.itr = null;
                                            } else {
                                                this.yield(this.itr.next());
                                            }
                                        }
                                        if (this.itr == null && this.idx < fileCount) {
                                            File file = files[this.idx];
                                            boolean included = FileSelector.this.apply(file);
                                            ++this.idx;
                                            if (included) {
                                                this.yield(file);
                                            }
                                            if (file.isDirectory()) {
                                                this.itr = FileSelector.this.listDirectory(file, true, FileSelector.this).iterator();
                                                if (!included) {
                                                    this.generate();
                                                }
                                            }
                                        }
                                    }
                                }
                            };
                        }
                    });
                }
            }
        }
        return ret;
    }

    public static final FileSelector SELECT_ALL = new FileSelector(new AbstractUnaryPredicate<File>() {

        public boolean apply(File _) {
            return true;
        }
    });

    public static final FileSelector SELECT_NONE = new FileSelector(new AbstractUnaryPredicate<File>() {

        public boolean apply(File _) {
            return false;
        }
    });

    public static final FileSelector SELECT_FILES = new FileSelector(new AbstractUnaryPredicate<File>() {

        public boolean apply(File file) {
            return (file != null && file.isFile());
        }
    });

    public static final FileSelector SELECT_DIRECTORIES = new FileSelector(new AbstractUnaryPredicate<File>() {

        public boolean apply(File file) {
            return (file != null && file.isDirectory());
        }
    });
}
