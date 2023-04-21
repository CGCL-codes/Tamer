package net.sf.kfgodel.bean2bean.population.instructions;

import net.sf.kfgodel.bean2bean.conversion.TypeConverter;
import net.sf.kfgodel.bean2bean.population.conversion.ConversionInstruction;
import net.sf.kfgodel.bean2bean.population.getting.GetterInstruction;
import net.sf.kfgodel.bean2bean.population.setting.SetterInstruction;

/**
 * Esta interfaz representa una instruccion de populacion que sabe como obtener el dato de un objeto
 * y asignarlo a otro
 * 
 * @version 1.0
 * @since 25/12/2007
 * @author D. Garcia
 */
public interface PopulationInstruction {

    /**
	 * @return Sub-instruccion que sabe como obtener el valor
	 */
    GetterInstruction getGetterInstruction();

    /**
	 * @return Sub-intruccion que sabe como convertir el valor al tipo esperado
	 */
    ConversionInstruction getConversionInstruction();

    /**
	 * @return Sub-instruccion que sabe como asignar el valor en destino
	 */
    SetterInstruction getSetterInstruction();

    /**
	 * Aplica esta instruccion para popular los objetos pasados
	 * 
	 * @param source
	 *            Objeto origen de la populacion, sobre el que se aplicará el getter
	 * @param destination
	 *            Objeto sobre el que se aplicara el setter
	 * @param typeConverter
	 *            Conversor de tipo para la conversion
	 */
    void applyOn(Object source, Object destination, TypeConverter typeConverter);
}
