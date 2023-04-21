package com.w20e.socrates.process;

import java.io.Serializable;
import com.w20e.socrates.workflow.Processor;

/**
 * @author dokter
 *
 * A Runner is used as a central focus point in a session within
 * socrates. It can be used to request rendering and store data. The
 * Runner is a Processor: it supports steps in the process of
 * rendering pre- and postprocessing, and rendering a questionnaire.
 */
public interface Runner extends Serializable, Processor {
}
