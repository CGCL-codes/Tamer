package mipt.aaf.edit.form;

/**
 * The main method is notified by FieldEditor or by Form when some field of the form is changing.
 * fieldActionPerformed() method is called by some editors on action events.
 * Is not only listener:
 *  1) can forbid changing by throwing FieldVetoException
 *  2) corrects proposing value - returns right value not for editor (e.g. for a model)
 * // Note: we do not use VetoableChangeListener & -//-Support because support is very inefficient
 * //  and too symmetric for their listeners (e.g. we have to change values back in field editors but not
 * // in form or in specific editor; we khow who can forbid change and who only reflects change (field editor))
 * @author Evdokimov
 */
public interface FieldListener {

    /**
	 * Called to propose new value (can be changed or prohibited here).
	 * Always returns right value (if no exception).
	 * @param field
	 * @param fieldValue
	 * @param model can be used to get old value; this argument is sent by form, not by editor itself
	 * @throws FieldVetoException - contains correct editor value
	 */
    Object fieldChanging(Field field, Object fieldValue, FieldModel model) throws FieldVetoException;

    /**
	 * Called if some action event is occured in the editor (not connected with main event).
	 */
    void fieldActionPerformed(Field field);
}
