package jode.test;

/**
 * This class tests name conflicts and their resolvation.  Note that every
 * name in this file should be the shortest possible name.
 */
public class Conflicts {

    int Conflicts;

    class Blah {

        Conflicts Inner;

        void Conflicts() {
            Inner = jode.test.Conflicts.this;
        }
    }

    class Inner {

        int Conflicts;

        Conflicts Inner;

        class Blah extends Conflicts.Blah {

            int Blah;

            void Inner() {
                this.Inner.Inner();
                this.Conflicts();
            }

            Blah() {
            }

            Blah(Conflicts Conflicts) {
                Conflicts.super();
            }
        }

        void Conflicts() {
            int Conflicts = 4;
            Conflicts();
            new Object() {

                void Inner() {
                    jode.test.Conflicts.this.Inner();
                }
            };
            this.Conflicts = Conflicts;
            Inner();
            jode.test.Conflicts.this.Conflicts = this.Conflicts;
        }

        Conflicts Conflicts(Inner Conflicts) {
            return jode.test.Conflicts.this;
        }
    }

    class Second extends Conflicts.Inner.Blah {

        Inner Blah = new Inner();

        class Inner extends Conflicts.Inner {
        }

        Conflicts.Inner create() {
            return jode.test.Conflicts.this.new Inner();
        }

        Second(Conflicts.Inner Blah) {
            Blah.super();
        }
    }

    public void Inner() {
    }

    public Conflicts() {
        int Conflicts = this.Conflicts;
        Inner Inner = new Inner();
        Inner.Conflicts = 5;
        new Inner().Conflicts(Inner).Inner();
    }
}
