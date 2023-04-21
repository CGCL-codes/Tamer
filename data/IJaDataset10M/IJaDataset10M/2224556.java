package rl.taskchanger;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * <h5>TaskChanger�T�[�r�X�N���N���X</h5>
 * <p>
 * <table>
 * <tbody>
 * <tr>
 * 		<td>��{�I�ȋ@�\<td>
 * </tr>
 * <tr>
 * 	<td><li>OS�N�����ɃT�[�r�X�Ƃ��ċN������</td>
 * </tr>
 * </tbody>
 * </table>

 *
 * @author F.O
 */
public class TriggerOnBoot extends BroadcastReceiver {

    /**
     * OS�N������ShakeDetector���T�[�r�X�Ƃ��ď풓������
     * @param context �R���e�L�X�g
     * @param intent �C���e���g
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent newIntent = new Intent(Intent.ACTION_VIEW);
            newIntent = new Intent(context, ShakeDetector.class);
            context.startService(newIntent);
        }
    }
}
