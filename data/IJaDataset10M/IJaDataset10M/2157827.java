package org.monet.bpi;

import org.monet.bpi.types.Check;

public interface BPIFieldCheck extends BPIField<Check> {

    boolean isChecked();
}
