package vse.core;

import com.conicsoft.bdkJ.core.IEventObject;
import com.conicsoft.bdkJ.gui.Defines.enumControlType;
import com.conicsoft.bdkJ.parser.Defines.enumConfigFileFormat;

public interface IWorkbench extends IControl {

    public IControl get_root();

    public IStemStream get_stem();

    public IControl create_control(enumControlType __type, IControl __parent);

    public void render();

    public boolean serial_load(String __file, enumConfigFileFormat __format);

    public boolean serial_save(String __file, enumConfigFileFormat __format);

    public void SetRenderListener(IEventObject evt);
}
