package jp.ac.osaka_u.ist.sel.metricstool.main.data.accessor;

import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.ClassMetricsInfoManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.metric.MetricAlreadyRegisteredException;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetClassInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin;
import jp.ac.osaka_u.ist.sel.metricstool.main.plugin.AbstractPlugin.PluginInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.util.METRIC_TYPE;

/**
 * �v���O�C�����N���X���g���N�X��o�^���邽�߂ɗp����N���X�D
 * 
 * @author higo
 */
public class DefaultClassMetricsRegister implements ClassMetricsRegister {

    /**
     * �o�^�����p�̃I�u�W�F�N�g�̏�����s���D�v���O�C���͎��g����Ƃ��ė^���Ȃ���΂Ȃ�Ȃ��D
     * 
     * @param plugin ������s���v���O�C���̃C���X�^���X
     */
    public DefaultClassMetricsRegister(final AbstractPlugin plugin) {
        if (null == plugin) {
            throw new NullPointerException();
        }
        final PluginInfo pluginInfo = plugin.getPluginInfo();
        if (METRIC_TYPE.CLASS_METRIC != pluginInfo.getMetricType()) {
            throw new IllegalArgumentException("plugin must be class type!");
        }
        this.plugin = plugin;
    }

    /**
     * ����̃N���X�̃��g���N�X�l�i����j��o�^����
     * @param classInfo ���g���N�X��o�^����N���X
     * @param value �o�^���郁�g���N�X�l
     * @throws MetricAlreadyRegisteredException ���łɃ��g���N�X���o�^����Ă���ꍇ�ɃX���[������O
     */
    @Override
    public void registMetric(final TargetClassInfo classInfo, final Number value) throws MetricAlreadyRegisteredException {
        if ((null == classInfo) || (null == value)) {
            throw new NullPointerException();
        }
        final ClassMetricsInfoManager manager = DataManager.getInstance().getClassMetricsInfoManager();
        manager.putMetric(classInfo, this.plugin, value);
    }

    private final AbstractPlugin plugin;
}
