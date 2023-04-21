package bagaturchess.learning.impl.eval.cfg;

public interface Weights_100ELO {

    public static final double MATERIAL_PAWN_O = 92.70370914932393;

    public static final double MATERIAL_PAWN_E = 109.27097101237743;

    public static final double MATERIAL_KNIGHT_O = 448.92539901892627;

    public static final double MATERIAL_KNIGHT_E = 378.1545744925371;

    public static final double MATERIAL_BISHOP_O = 451.2420762577397;

    public static final double MATERIAL_BISHOP_E = 425.4322302800047;

    public static final double MATERIAL_ROOK_O = 610.2191782838275;

    public static final double MATERIAL_ROOK_E = 685.4830831253004;

    public static final double MATERIAL_QUEEN_O = 1351.1638146963028;

    public static final double MATERIAL_QUEEN_E = 1252.148623150277;

    public static final double KINGSAFE_CASTLING_O = 7.434908343174102;

    public static final double KINGSAFE_CASTLING_E = 0.0;

    public static final double KINGSAFE_FIANCHETTO_O = 0.0;

    public static final double KINGSAFE_FIANCHETTO_E = 0.0;

    public static final double BISHOPS_DOUBLE_O = 38.27532446734872;

    public static final double BISHOPS_DOUBLE_E = 57.97795716638175;

    public static final double KINGSAFE_F_O = -5.529983109367203;

    public static final double KINGSAFE_F_E = 0.0;

    public static final double KINGSAFE_G_O = 0.0;

    public static final double KINGSAFE_G_E = 0.0;

    public static final double PAWNS_DOUBLED_O = -0.043700516207345226;

    public static final double PAWNS_DOUBLED_E = -15.111206266394412;

    public static final double PAWNS_ISOLATED_O = -13.046185497368544;

    public static final double PAWNS_ISOLATED_E = -12.027855842808483;

    public static final double PAWNS_BACKWARD_O = -5.614207121194952;

    public static final double PAWNS_BACKWARD_E = -2.2230574251042152;

    public static final double PAWNS_SUPPORTED_O = 5.728844702707407;

    public static final double PAWNS_SUPPORTED_E = 3.6567100650438533;

    public static final double PAWNS_CANNOTBS_O = -0.09234753338205168;

    public static final double PAWNS_CANNOTBS_E = -0.8415735757496994;

    public static final double PAWNS_PASSED_O = 3.0632835489861523;

    public static final double PAWNS_PASSED_E = 2.8409284679660987;

    public static final double PAWNS_PASSED_RNK_O = 1.2084877807020749;

    public static final double PAWNS_PASSED_RNK_E = 1.4894144542901115;

    public static final double PAWNS_UNSTOPPABLE_PASSER_O = 0.0;

    public static final double PAWNS_UNSTOPPABLE_PASSER_E = 550.0;

    public static final double PAWNS_CANDIDATE_RNK_O = 2.0;

    public static final double PAWNS_CANDIDATE_RNK_E = 1.3632597889721145;

    public static final double KINGS_PASSERS_F_O = 0.0;

    public static final double KINGS_PASSERS_F_E = 2.008351683214559;

    public static final double KINGS_PASSERS_FF_O = 0.0;

    public static final double KINGS_PASSERS_FF_E = 1.0650364261035525;

    public static final double KINGS_PASSERS_F_OP_O = 0.0;

    public static final double KINGS_PASSERS_F_OP_E = 1.7378931713971437;

    public static final double PAWNS_ISLANDS_O = -1.7802224142461838;

    public static final double PAWNS_ISLANDS_E = -0.7648020394030626;

    public static final double PAWNS_GARDS_O = 21.250633508081485;

    public static final double PAWNS_GARDS_E = 0.0;

    public static final double PAWNS_GARDS_REM_O = -8.468294412405967;

    public static final double PAWNS_GARDS_REM_E = 0.0;

    public static final double PAWNS_STORMS_O = 4.1408001114437285;

    public static final double PAWNS_STORMS_E = 0.0;

    public static final double PAWNS_STORMS_CLS_O = 2.2624463687722107;

    public static final double PAWNS_STORMS_CLS_E = 0.0;

    public static final double PAWNS_OPENNED_O = -33.82215354412606;

    public static final double PAWNS_OPENNED_E = 0.0;

    public static final double PAWNS_SEMIOP_OWN_O = -23.25744685575884;

    public static final double PAWNS_SEMIOP_OWN_E = 0.0;

    public static final double PAWNS_SEMIOP_OP_O = -12.452036370986857;

    public static final double PAWNS_SEMIOP_OP_E = 0.0;

    public static final double PAWNS_WEAK_O = -1.4378672936959358;

    public static final double PAWNS_WEAK_E = -0.03280050434967355;

    public static final double SPACE_O = 0.4770719238955803;

    public static final double SPACE_E = 1.096680431593482;

    public static final double ROOK_INFRONT_PASSER_O = -24.547052090357674;

    public static final double ROOK_INFRONT_PASSER_E = -0.5797674500182306;

    public static final double ROOK_BEHIND_PASSER_O = 0.07214846829563296;

    public static final double ROOK_BEHIND_PASSER_E = 0.06323531631526508;

    public static final double PST_PAWN_O = 0.49987630562882723;

    public static final double PST_PAWN_E = 0.9033291992595642;

    public static final double PST_KING_O = 1.313039454771462;

    public static final double PST_KING_E = 1.6746557063143583;

    public static final double PST_KNIGHTS_O = 1.1310752949913518;

    public static final double PST_KNIGHTS_E = 1.1731226610466932;

    public static final double PST_BISHOPS_O = 1.1116146956509574;

    public static final double PST_BISHOPS_E = 1.5329001212445807;

    public static final double PST_ROOKS_O = 2.0;

    public static final double PST_ROOKS_E = 0.6837856963776835;

    public static final double PST_QUEENS_O = 0.4176022262135228;

    public static final double PST_QUEENS_E = 1.255214836234484;

    public static final double BISHOPS_BAD_O = -0.4106198062446613;

    public static final double BISHOPS_BAD_E = -0.6209238452006848;

    public static final double KNIGHT_OUTPOST_O = 0;

    public static final double KNIGHT_OUTPOST_E = 0;

    public static final double ROOKS_OPENED_O = 28.000308428982883;

    public static final double ROOKS_OPENED_E = 3.9719675389999995;

    public static final double ROOKS_SEMIOPENED_O = 21.095938816435996;

    public static final double ROOKS_SEMIOPENED_E = 1.052416713412923;

    public static final double TROPISM_KNIGHT_O = 0;

    public static final double TROPISM_KNIGHT_E = 0;

    public static final double TROPISM_BISHOP_O = 0;

    public static final double TROPISM_BISHOP_E = 0;

    public static final double TROPISM_ROOK_O = 0;

    public static final double TROPISM_ROOK_E = 0;

    public static final double TROPISM_QUEEN_O = 0;

    public static final double TROPISM_QUEEN_E = 0;

    public static final double ROOKS_7TH_2TH_O = 9.644969284270207;

    public static final double ROOKS_7TH_2TH_E = 27.63858554105413;

    public static final double QUEENS_7TH_2TH_O = 0;

    public static final double QUEENS_7TH_2TH_E = 0;

    public static final double KINGSAFETY_L1_O = 53.132738521983555;

    public static final double KINGSAFETY_L1_E = 0.0;

    public static final double KINGSAFETY_L2_O = 10.957861677658057;

    public static final double KINGSAFETY_L2_E = 0.0;

    public static final double MOBILITY_KNIGHT_O = 0.8943116952443472;

    public static final double MOBILITY_KNIGHT_E = 1.471020950222214;

    public static final double MOBILITY_BISHOP_O = 1.2481064012811574;

    public static final double MOBILITY_BISHOP_E = 1.5298749309687538;

    public static final double MOBILITY_ROOK_O = 1.0018086214434443;

    public static final double MOBILITY_ROOK_E = 1.7431039403980697;

    public static final double MOBILITY_QUEEN_O = 0.1623860748379441;

    public static final double MOBILITY_QUEEN_E = 0.9659483503232686;

    public static final double MOBILITY_KNIGHT_S_O = 0.7998879769662636;

    public static final double MOBILITY_KNIGHT_S_E = 1.2415520322862512;

    public static final double MOBILITY_BISHOP_S_O = 0.8119796945437265;

    public static final double MOBILITY_BISHOP_S_E = 0.5720452205443163;

    public static final double MOBILITY_ROOK_S_O = 0.4292660368278465;

    public static final double MOBILITY_ROOK_S_E = 1.6664999778667902;

    public static final double MOBILITY_QUEEN_S_O = 0.20610678915235117;

    public static final double MOBILITY_QUEEN_S_E = 0.6930695749958158;

    public static final double ROOKS_PAIR_H_O = 7.435355381255075;

    public static final double ROOKS_PAIR_H_E = 0.08192967537297793;

    public static final double ROOKS_PAIR_V_O = 0.025543926466817204;

    public static final double ROOKS_PAIR_V_E = 0.013044162666657916;

    public static final double TRAP_KNIGHT_O = -0.6173853562692815;

    public static final double TRAP_KNIGHT_E = -0.7436439210321998;

    public static final double TRAP_BISHOP_O = -0.40353989877003454;

    public static final double TRAP_BISHOP_E = -0.862587878329959;

    public static final double TRAP_ROOK_O = -0.08611977316501628;

    public static final double TRAP_ROOK_E = -3.7211025676592797;

    public static final double TRAP_QUEEN_O = 0.0;

    public static final double TRAP_QUEEN_E = 0.0;

    public static final double PIN_BK_O = 20.509087958664736;

    public static final double PIN_BK_E = 3.2091078994385476;

    public static final double PIN_BQ_O = 5.076803796520375;

    public static final double PIN_BQ_E = 12.853216754458513;

    public static final double PIN_BR_O = 5.933711893702372;

    public static final double PIN_BR_E = 3.1507528061534273;

    public static final double PIN_BN_O = 3.453038500191905;

    public static final double PIN_BN_E = 1.611070401246477;

    public static final double PIN_RK_O = 0.01761705544559072;

    public static final double PIN_RK_E = 1.3766741127970092;

    public static final double PIN_RQ_O = 0.7252918047850787;

    public static final double PIN_RQ_E = 30.78706235094331;

    public static final double PIN_RB_O = 0.023990216375843865;

    public static final double PIN_RB_E = 2.698630894930929;

    public static final double PIN_RN_O = 0.12587568457263057;

    public static final double PIN_RN_E = 3.96755999877084;

    public static final double PIN_QK_O = 3.8468611576411225;

    public static final double PIN_QK_E = 0.4137937133314934;

    public static final double PIN_QQ_O = 1.0007380796571386;

    public static final double PIN_QQ_E = 12.741144014472525;

    public static final double PIN_QN_O = 3.4701228055989377;

    public static final double PIN_QN_E = 0.13902500667733053;

    public static final double PIN_QR_O = 1.5573595610532347;

    public static final double PIN_QR_E = 0.915836193327515;

    public static final double PIN_QB_O = 0.024103070840024294;

    public static final double PIN_QB_E = 6.764587501794264;

    public static final double ATTACK_BN_O = 5.246244685603124;

    public static final double ATTACK_BN_E = 18.364024543925144;

    public static final double ATTACK_BR_O = 7.013790496002545;

    public static final double ATTACK_BR_E = 36.03715020406101;

    public static final double ATTACK_NB_O = 9.02219573757875;

    public static final double ATTACK_NB_E = 13.943738803310954;

    public static final double ATTACK_NR_O = 0.04684969974009867;

    public static final double ATTACK_NR_E = 26.397522301917423;

    public static final double ATTACK_NQ_O = 0.7861211767395059;

    public static final double ATTACK_NQ_E = 37.86539211080439;

    public static final double ATTACK_RB_O = 1.7080053720634965;

    public static final double ATTACK_RB_E = 14.120401475099762;

    public static final double ATTACK_RN_O = 2.7886161839630805;

    public static final double ATTACK_RN_E = 18.97522188561195;

    public static final double ATTACK_QN_O = 1.0093486705149997;

    public static final double ATTACK_QN_E = 11.33367980142347;

    public static final double ATTACK_QB_O = 1.708565426137552;

    public static final double ATTACK_QB_E = 2.0200180338290674;

    public static final double ATTACK_QR_O = 7.280547136957392;

    public static final double ATTACK_QR_E = 10.761839231294978;

    public static final double HUNGED_PIECE_O = -7.806568685766735;

    public static final double HUNGED_PIECE_E = -16.598826975511976;
}
